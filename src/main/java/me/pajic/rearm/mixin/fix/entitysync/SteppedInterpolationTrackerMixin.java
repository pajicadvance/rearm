package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.entity.SteppedInterpolationTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SteppedInterpolationTracker.class)
public class SteppedInterpolationTrackerMixin {

	@Shadow @Final private Entity entity;

	@ModifyReturnValue(method = "getPositionPath", at = @At("RETURN"))
	private PositionPath entitySyncFix_setStartImmediately(PositionPath original) {
		if (ReArm.CONFIG.bugFixes.entitySyncFix.get() && original instanceof PositionPath.Stepped) {
			((SteppedPathExtension) (Object) original).rearm$setStartImmediately(entity.needsSync);
		}
		return original;
	}
}
//?}
