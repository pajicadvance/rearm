package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.network.protocol.game.VecDelta;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.world.entity.PositionPath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VecDeltaCodec.class)
public class VecDeltaCodecMixin {

	@ModifyReturnValue(method = "tryEncode(Lnet/minecraft/world/entity/PositionPath;)Lnet/minecraft/network/protocol/game/VecDelta;", at = @At("RETURN"))
	private VecDelta entitySyncFix_copyStartImmediately(VecDelta original, PositionPath position) {
		if (original instanceof VecDelta.Stepped && position instanceof PositionPath.Stepped) {
            SteppedPathExtension vecExt = (SteppedPathExtension) (Object) original;
            SteppedPathExtension posExt = (SteppedPathExtension) (Object) position;
			vecExt.rearm$setStartImmediately((posExt).rearm$startImmediately());
		}
		return original;
	}
}
//?}
