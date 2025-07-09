package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.CriticalCounterAbility;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(value = Player.class, priority = 250)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArgs(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            )
    )
    private void sweepingEdge_increaseAttackRadius(Args args) {
        if (Main.CONFIG.sword.improvedSweepingEdge.get()) {
            int sweepingEdgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.SWEEPING_EDGE),
                    getWeaponItem()
            );
            if (sweepingEdgeLevel > 0) {
                args.set(0, (double) args.get(0) * sweepingEdgeLevel);
                args.set(2, (double) args.get(2) * sweepingEdgeLevel);
                if (sweepingEdgeLevel >= 3) {
                    args.set(1, 1.0);
                }
            }
        }
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private <T extends Entity> List<T> sweepingEdge_getHitEntities(
            List<T> original, @Share("original") LocalRef<List<T>> hitEntityList
    ) {
        if (Main.CONFIG.sword.improvedSweepingEdge.get()) {
            hitEntityList.set(original);
        }
        return original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 1
    )
    private <T extends Entity> float sweepingEdge_increaseDamage(
            float damage, @Share("original") LocalRef<List<T>> hitEntityList, @Local(ordinal = 2) float h
    ) {
        if (Main.CONFIG.sword.improvedSweepingEdge.get()) {
            float additionalDamage = Main.CONFIG.sword.sweepingEdgeAdditionalDamagePerMob.get() *
                    Math.min(hitEntityList.get().size() - 1, Main.CONFIG.sword.maxMobAmountUsedForDamageIncrease.get());
            return damage + additionalDamage * h;
        }
        return damage;
    }

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 2
    )
    private boolean criticalCounter_critOnlyIfCriticalCounter(boolean original) {
        if (
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                CriticalCounterAbility.canCounter(getWeaponItem())
        ) {
            return CriticalCounterAbility.getPlayerCounterCondition(serverPlayer.getUUID());
        }
        return original;
    }

    @WrapMethod(method = "hurtCurrentlyUsedShield")
    private void criticalCounter_startTimer(float damageAmount, Operation<Void> original) {
        original.call(damageAmount);
        if (
                damageAmount >= 3.0F && !useItem.isEmpty() &&
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                CriticalCounterAbility.canCounter(getWeaponItem())
        ) {
            ServerPlayNetworking.send(serverPlayer, new CriticalCounterAbility.S2CStartCriticalCounterTimer());
        }
    }

    @ModifyExpressionValue(
            method = "getProjectile",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
            )
    )
    private boolean infinityFix(boolean original, @Local(argsOnly = true) ItemStack weaponStack) {
        if (Main.CONFIG.tweaks.infinityFix.get()) {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.INFINITY),
                    weaponStack
            );
            return original || infinityLevel > 0;
        }
        return original;
    }
}
