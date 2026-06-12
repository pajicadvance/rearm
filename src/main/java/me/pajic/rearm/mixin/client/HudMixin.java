package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Hud.class)
public class HudMixin {

	@ModifyExpressionValue(
			method = "extractEffects",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/effect/MobEffectInstance;showIcon()Z"
			)
	)
	private boolean hideBackstepDisplay(boolean original, @Local(name = "instance") MobEffectInstance instance) {
		if (ReArm.CONFIG.bow.enableBackstep.get() && instance.is(ReArmEffects.BACKSTEP_EFFECT)) return false;
		return original;
	}

	@ModifyExpressionValue(
			method = "extractArmor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"
			)
	)
	private static int scaleArmorBar(int original) {
		return CompatFlags.armorRebalanceActive() ? Math.round(original / ReArm.CONFIG.armor.armorMultiplier.get()) : original;
	}
}
