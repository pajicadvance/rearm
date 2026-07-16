package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.damagesource.ReArmDamageTypes;
import me.pajic.rearm.extension.AbstractArrowExtension;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {

	@ModifyExpressionValue(
			method = "arrow",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/damagesource/DamageTypes;ARROW:Lnet/minecraft/resources/ResourceKey;",
					opcode = Opcodes.GETSTATIC
			)
	)
	private ResourceKey<DamageType> setMultishotArrow(
			ResourceKey<DamageType> original,
			@Local(argsOnly = true, name = "arrow") final AbstractArrow arrow
	) {
		return ((AbstractArrowExtension) arrow).rearm$isMultishotArrow() ? ReArmDamageTypes.MULTISHOT_ARROW : original;
	}
}
