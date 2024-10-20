package me.pajic.ranger.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.ranger.Main;
import me.pajic.ranger.ability.AbilityNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    @WrapOperation(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processProjectileSpread(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;F)F"
            )
    )
    private float multishotAbility_shoot(ServerLevel level, ItemStack weapon, Entity entity, float projectileSpread, Operation<Float> original) {
        if (Main.CONFIG.abilities.multishotAbility()) {
            if (AbilityNetworking.shouldTriggerMultishot(weapon, entity)) {
                if (entity instanceof ServerPlayer player) {
                    ServerPlayNetworking.send(player, new AbilityNetworking.S2CClearAbilityUsedFlagPayload());
                }
                return original.call(level, weapon, entity, projectileSpread);
            }
            return 0;
        }
        return original.call(level, weapon, entity, projectileSpread);
    }

    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processProjectileCount(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;I)I"
            )
    )
    private static int multishotAbility_draw(ServerLevel level, ItemStack weapon, Entity entity, int projectileCount, Operation<Integer> original) {
        if (Main.CONFIG.abilities.multishotAbility()) {
            if (AbilityNetworking.shouldTriggerMultishot(weapon, entity)) {
                return original.call(level, weapon, entity, projectileCount);
            }
            return 1;
        }
        return original.call(level, weapon, entity, projectileCount);
    }

    @Inject(
            method = "shoot",
            at = @At("TAIL")
    )
    private void resetReadyFlagOnUse(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target, CallbackInfo ci) {
        if (AbilityNetworking.shouldTriggerMultishot(weapon, shooter)) {
            if (shooter instanceof ServerPlayer player) {
                ServerPlayNetworking.send(player, new AbilityNetworking.S2CClearClientMultishotReadyFlagPayload());
            }
            AbilityNetworking.MULTISHOT_READY_SERVER = false;
        }
    }
}
