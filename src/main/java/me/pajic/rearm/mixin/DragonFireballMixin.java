package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DragonFireball.class)
public class DragonFireballMixin {

	@WrapOperation(
			method = "onHit",
			at = @At(
					value = "NEW",
					target = "(Lnet/minecraft/core/Holder;II)Lnet/minecraft/world/effect/MobEffectInstance;"
			)
	)
	private MobEffectInstance fixDragonBreath(Holder<MobEffect> effect, int duration, int amplifier, Operation<MobEffectInstance> original) {
		return original.call(ReArm.CONFIG.bugFixes.dragonBreathDamageTypeFix.get() ? ReArmEffects.DRAGON_BREATH : effect, duration, amplifier);
	}
}
