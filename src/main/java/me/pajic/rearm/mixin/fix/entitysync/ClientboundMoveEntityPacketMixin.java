package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.VecDelta;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ClientboundMoveEntityPacket.Pos.class, ClientboundMoveEntityPacket.PosRot.class})
public class ClientboundMoveEntityPacketMixin {

	@WrapOperation(
			method = "read",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/game/VecDelta;read(Lnet/minecraft/network/FriendlyByteBuf;I)Lnet/minecraft/network/protocol/game/VecDelta;"
			)
	)
	private static VecDelta entitySyncFix_readStartImmediately(FriendlyByteBuf input, int stepCount, Operation<VecDelta> original) {
		boolean startImmediately = stepCount > 0 && input.readBoolean();
		VecDelta delta = original.call(input, stepCount);
		if (delta instanceof VecDelta.Stepped) {
			((SteppedPathExtension) (Object) delta).rearm$setStartImmediately(startImmediately);
		}
		return delta;
	}

	@WrapOperation(
			method = "write",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/protocol/game/VecDelta;write(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/network/protocol/game/VecDelta;)V"
			)
	)
	private void entitySyncFix_writeStartImmediately(FriendlyByteBuf output, VecDelta delta, Operation<Void> original) {
		if (delta.stepCount() > 0) {
			output.writeBoolean(delta instanceof VecDelta.Stepped && ((SteppedPathExtension) (Object) delta).rearm$startImmediately());
		}
		original.call(output, delta);
	}
}
//?}
