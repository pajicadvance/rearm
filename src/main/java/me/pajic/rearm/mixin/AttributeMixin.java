package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.ReArm;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Attributes.class)
public class AttributeMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=30.0"
            )
    )
    private static double increaseArmorCap(double original) {
        return ReArm.CONFIG.armor.armorRebalance.get() ? 20 * ReArm.CONFIG.armor.armorMultiplier.get() : original;
    }
}
