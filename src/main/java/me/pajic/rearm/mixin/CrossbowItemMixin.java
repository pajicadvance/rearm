package me.pajic.rearm.mixin;

import me.pajic.rearm.Main;
import me.pajic.rearm.ability.AbilityManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyArg(
            method = "getChargeDuration",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;modifyCrossbowChargingTime(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;F)F"
            ),
            index = 2
    )
    private static float modifyCrossbowChargingTime(float original) {
        if (Main.CONFIG.crossbow.modifyLoadSpeed()) {
            return Main.CONFIG.crossbow.loadTime();
        }
        return original;
    }

    @Inject(
            method = "performShooting",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private void piercingShotAbility_shoot(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, float velocity, float inaccuracy, LivingEntity target, CallbackInfo ci) {
        if (Main.CONFIG.abilities.piercingShotAbility()) {
            if (
                    shooter instanceof ServerPlayer player &&
                    AbilityManager.piercingShotAbility.shouldTriggerAbility(weapon, player)
            ) {
                AbilityManager.setPlayerAbilityUsed(player);
            }
        }
    }
}
