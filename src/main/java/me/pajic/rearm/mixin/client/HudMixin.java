package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? fabric && <26.1 {
/*import com.mojang.blaze3d.systems.RenderSystem;
import me.pajic.rearm.hud.ItemUseProgressBars;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Mixin(Hud.class)
public class HudMixin {

    @ModifyExpressionValue(
			method = {"extractEffects", "renderEffects"},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/effect/MobEffectInstance;showIcon()Z"
			)
	)
	private boolean hideBackstepDisplay(boolean original, @Local MobEffectInstance instance) {
		if (ReArm.CONFIG.bow.enableBackstep.get() && instance.is(ReArmEffects.BACKSTEP_EFFECT)) return false;
		return original;
	}

	@ModifyExpressionValue(
			method = {"extractArmor", "renderArmor"},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"
			)
	)
	private static int scaleArmorBar(int original) {
		return CompatFlags.armorRebalanceActive() ? Math.round(original / ReArm.CONFIG.armor.armorMultiplier.get()) : original;
	}

    //? fabric && <26.1 {
    /*@Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "renderExperienceBar",
            at = @At("TAIL")
    )
    private void renderProgressBars(GuiGraphicsExtractor guiGraphics, int i, CallbackInfo ci) {
        minecraft.getProfiler().push("itemUseProgressBar");
        RenderSystem.enableBlend();
        ItemUseProgressBars.BARS.forEach(bar -> bar.extractBackground(guiGraphics));
        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
    *///?}
}
