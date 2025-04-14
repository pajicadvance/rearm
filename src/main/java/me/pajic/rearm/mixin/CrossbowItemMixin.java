package me.pajic.rearm.mixin;

import me.pajic.rearm.Main;
import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
}
