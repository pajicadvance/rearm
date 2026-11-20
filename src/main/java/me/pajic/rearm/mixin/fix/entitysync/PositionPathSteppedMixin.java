package me.pajic.rearm.mixin.fix.entitysync;

//? >=26.3 {

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.netty.buffer.ByteBuf;
import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.PositionPath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PositionPath.Stepped.class)
public abstract class PositionPathSteppedMixin implements SteppedPathExtension {

	@Unique private boolean rearm$startImmediately;

	@Override
	public boolean rearm$startImmediately() {
		return rearm$startImmediately;
	}

	@Override
	public void rearm$setStartImmediately(boolean startImmediately) {
		rearm$startImmediately = startImmediately;
	}

	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;"
			)
	)
	private static StreamCodec<ByteBuf, PositionPath.Stepped> entitySyncFix_addStartImmediately(
            StreamCodec<ByteBuf, PositionPath.Stepped> original
    ) {
		return StreamCodec.of(
				(output, stepped) -> {
					original.encode(output, stepped);
					output.writeBoolean(((SteppedPathExtension) (Object) stepped).rearm$startImmediately());
				},
				input -> {
					PositionPath.Stepped stepped = original.decode(input);
					((SteppedPathExtension) (Object) stepped).rearm$setStartImmediately(input.readBoolean());
					return stepped;
				}
		);
	}
}
//?}
