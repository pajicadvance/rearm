package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.interpolation.BackportedSteppedInterpolationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.SteppedInterpolationHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SteppedInterpolationHandler.class)
public class SteppedInterpolationHandlerMixin {

	@WrapMethod(method = "create")
	private static InterpolationHandler entitySyncFix_useBackportedHandler(Entity entity, Operation<InterpolationHandler> original) {
		if (ReArm.CONFIG.bugFixes.entitySyncFix.get() && entity.level().isClientSide()) {
			return new BackportedSteppedInterpolationHandler(entity, entity instanceof Player ? 3 : entity.getType().updateInterval());
		}
		return original.call(entity);
	}
}
//?}
