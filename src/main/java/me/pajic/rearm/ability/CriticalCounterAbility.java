package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CriticalCounterAbility {

    public static final ResourceLocation COUNTER_START_TIMER = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "counter_start_timer");
    public static final ResourceLocation UPDATE_PLAYER_COUNTER_CONDITION = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "update_player_counter_condition");

    public record C2SUpdatePlayerCounterCondition(UUID activePlayerUUID, boolean shouldCounter) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2SUpdatePlayerCounterCondition> TYPE = new CustomPacketPayload.Type<>(UPDATE_PLAYER_COUNTER_CONDITION);
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
        public static final CustomPacketPayload.Type<S2CStartCriticalCounterTimer> TYPE = new CustomPacketPayload.Type<>(COUNTER_START_TIMER);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CStartCriticalCounterTimer> CODEC = StreamCodec.unit(
                new S2CStartCriticalCounterTimer()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private static final Object2BooleanOpenHashMap<UUID> playerCounterConditions = new Object2BooleanOpenHashMap<>();

    public static void setPlayerCounterCondition(UUID playerUUID, boolean shouldCounter) {
        if (playerCounterConditions.containsKey(playerUUID)) {
            playerCounterConditions.replace(playerUUID, shouldCounter);
        }
        else {
            playerCounterConditions.put(playerUUID, shouldCounter);
        }
    }

    public static boolean getPlayerCounterCondition(UUID playerUUID) {
        if (playerCounterConditions.containsKey(playerUUID)) {
            return playerCounterConditions.getBoolean(playerUUID);
        }
        return false;
    }

    public static void removePlayerCounterConditionData(UUID playerUUID) {
        playerCounterConditions.removeBoolean(playerUUID);
    }

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SUpdatePlayerCounterCondition.TYPE, C2SUpdatePlayerCounterCondition.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CStartCriticalCounterTimer.TYPE, S2CStartCriticalCounterTimer.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2SUpdatePlayerCounterCondition.TYPE, (payload, context) ->
                setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
        );
    }

    public static boolean canCounter(ItemStack stack) {
        return Main.CONFIG.sword.enableCriticalCounter.get() && stack.is(ItemTags.SWORDS) ||
                Main.CONFIG.axe.enableCriticalCounter.get() && stack.is(ItemTags.AXES);
    }
}
