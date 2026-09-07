package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import me.pajic.rearm.ReArm;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CriticalCounterAbility {

    public static final Identifier COUNTER_START_TIMER = ReArm.id("counter_start_timer");
    public static final Identifier UPDATE_PLAYER_COUNTER_CONDITION = ReArm.id("update_player_counter_condition");

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
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CStartCriticalCounterTimer> CODEC = StreamCodec.unit(new S2CStartCriticalCounterTimer());

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

    public static boolean canCounter(ItemStack stack) {
        return ReArm.CONFIG.sword.enableCriticalCounter.get() && stack.is(ItemTags.SWORDS) ||
				ReArm.CONFIG.axe.enableCriticalCounter.get() && stack.is(ItemTags.AXES);
    }

	public static boolean canVanillaCrit(ItemStack stack) {
		return !ReArm.CONFIG.sword.disableVanillaCrits.get() && stack.is(ItemTags.SWORDS) ||
				!ReArm.CONFIG.axe.disableVanillaCrits.get() && stack.is(ItemTags.AXES);
	}
}
