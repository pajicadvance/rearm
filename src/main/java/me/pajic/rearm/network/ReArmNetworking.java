package me.pajic.rearm.network;

import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReArmNetworking {

    public static final ResourceLocation BACKSTEP_EXHAUSTION = ResourceLocation.fromNamespaceAndPath("rearm", "backstep_exhaustion");
    public static final ResourceLocation COUNTER_START_TIMER = ResourceLocation.fromNamespaceAndPath("rearm", "counter_start_timer");
    public static final ResourceLocation UPDATE_PLAYER_COUNTER_CONDITION = ResourceLocation.fromNamespaceAndPath("rearm", "update_player_counter_condition");
    public static final ResourceLocation RECALL_AXE = ResourceLocation.fromNamespaceAndPath("rearm", "recall_axe");

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

    public record C2SUpdatePlayerCounterCondition(UUID activePlayerUUID, boolean shouldCounter) implements CustomPacketPayload {
        public static final Type<C2SUpdatePlayerCounterCondition> TYPE = new Type<>(UPDATE_PLAYER_COUNTER_CONDITION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdatePlayerCounterCondition> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SUpdatePlayerCounterCondition::activePlayerUUID,
                ByteBufCodecs.BOOL, C2SUpdatePlayerCounterCondition::shouldCounter,
                C2SUpdatePlayerCounterCondition::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2CStartCriticalCounterTimer() implements CustomPacketPayload {
        public static final Type<S2CStartCriticalCounterTimer> TYPE = new Type<>(COUNTER_START_TIMER);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CStartCriticalCounterTimer> CODEC = StreamCodec.unit(
                new S2CStartCriticalCounterTimer()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SUpdatePlayerRecallCondition(UUID activePlayerUUID) implements CustomPacketPayload {
        public static final Type<C2SUpdatePlayerRecallCondition> TYPE = new Type<>(RECALL_AXE);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdatePlayerRecallCondition> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SUpdatePlayerRecallCondition::activePlayerUUID,
                C2SUpdatePlayerRecallCondition::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void init(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                C2SCauseBackstepExhaustionPayload.TYPE,
                C2SCauseBackstepExhaustionPayload.CODEC,
                (payload, context) -> context.player().causeFoodExhaustion(payload.exhaustion)
        );
        registrar.playToServer(
                C2SUpdatePlayerCounterCondition.TYPE,
                C2SUpdatePlayerCounterCondition.CODEC,
                (payload, context) ->
                        CriticalCounterAbility.setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
        );
        registrar.playToServer(
                C2SUpdatePlayerRecallCondition.TYPE,
                C2SUpdatePlayerRecallCondition.CODEC,
                (payload, context) ->
                        CripplingThrowAbility.recallSignals.add(payload.activePlayerUUID)
        );
        registrar.playToClient(
                S2CStartCriticalCounterTimer.TYPE,
                S2CStartCriticalCounterTimer.CODEC,
                (payload, context) -> CooldownTracker.counterTimerActive = true
        );
    }
}
