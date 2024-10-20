package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {

    @ModifyExpressionValue(
            method = "renderEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffectInstance;showIcon()Z"
            )
    )
    private boolean hideBackstepDisplay(boolean original, @Local MobEffectInstance effect) {
        // Hide the backstep status effect icon
        // Since the backstep window is less than a second it's really not necessary
        if (effect.is(ReArmEffects.BACKSTEP_EFFECT)) {
            return false;
        }
        return original;
    }
}
