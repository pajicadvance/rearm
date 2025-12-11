package me.pajic.rearm.ability;

import me.pajic.rearm.ReArm;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class BackstepAbility {
    public static final Identifier BACKSTEP_EXHAUSTION = ReArm.id("backstep_exhaustion");

    public record C2SCauseBackstepExhaustionPayload(float exhaustion) implements CustomPacketPayload {
        public static final Type<C2SCauseBackstepExhaustionPayload> TYPE = new Type<>(BACKSTEP_EXHAUSTION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SCauseBackstepExhaustionPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, C2SCauseBackstepExhaustionPayload::exhaustion,
                C2SCauseBackstepExhaustionPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
