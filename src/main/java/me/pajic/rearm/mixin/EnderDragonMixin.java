package me.pajic.rearm.mixin;

import me.pajic.rearm.ReArm;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {

	@ModifyArg(
			method = "aiStep",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"
			),
			index = 1
	)
	private double fixVerticalVelocity(double y) {
		return ReArm.CONFIG.bugFixes.enderDragonVelocityFix.get() ? y * 10 : y;
	}
}
