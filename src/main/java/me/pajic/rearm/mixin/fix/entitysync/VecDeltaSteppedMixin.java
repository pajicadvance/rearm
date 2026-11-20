package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.network.protocol.game.VecDelta;
import net.minecraft.world.entity.PositionPath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VecDelta.Stepped.class)
public abstract class VecDeltaSteppedMixin implements SteppedPathExtension {

	@Unique private boolean rearm$startImmediately;

	@Override
	public boolean rearm$startImmediately() {
		return rearm$startImmediately;
	}

	@Override
	public void rearm$setStartImmediately(boolean startImmediately) {
		rearm$startImmediately = startImmediately;
	}

	@ModifyReturnValue(method = "decode", at = @At("RETURN"))
	private PositionPath entitySyncFix_copyStartImmediately(PositionPath original) {
		if (original instanceof PositionPath.Stepped) {
			((SteppedPathExtension) (Object) original).rearm$setStartImmediately(rearm$startImmediately);
		}
		return original;
	}
}
//?}
