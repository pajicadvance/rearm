package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
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
        if (ReArm.CONFIG.bow.enableBackstep.get() && effect.is(ReArmEffects.BACKSTEP_EFFECT)) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "renderArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"
            )
    )
    private static int scaleArmorBar(int original) {
        return ReArm.armorRebalanceActive() ? Math.round(original / ReArm.CONFIG.armor.armorMultiplier.get()) : original;
    }
}
