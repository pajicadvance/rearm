package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BackstepAbility {
    public static final ResourceLocation BACKSTEP_EXHAUSTION = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "backstep_exhaustion");

    public record C2SCauseBackstepExhaustionPayload(float exhaustion) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2SCauseBackstepExhaustionPayload> TYPE = new CustomPacketPayload.Type<>(BACKSTEP_EXHAUSTION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SCauseBackstepExhaustionPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, C2SCauseBackstepExhaustionPayload::exhaustion,
                C2SCauseBackstepExhaustionPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SCauseBackstepExhaustionPayload.TYPE, C2SCauseBackstepExhaustionPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                C2SCauseBackstepExhaustionPayload.TYPE,
                (payload, context) -> context.player().causeFoodExhaustion(payload.exhaustion)
        );
    }
}
