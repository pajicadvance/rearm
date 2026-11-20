package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.pajic.rearm.ReArm;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CriticalCounterAbility {

    private static final Object2LongOpenHashMap<UUID> counterStartTimes = new Object2LongOpenHashMap<>();

    public static void startCounter(ServerPlayer player) {
        counterStartTimes.put(player.getUUID(), player.level().getGameTime());
    }

    public static boolean isCounterActive(ServerPlayer player) {
        if (!counterStartTimes.containsKey(player.getUUID())) return false;
        long elapsed = player.level().getGameTime() - counterStartTimes.getLong(player.getUUID());
        return elapsed >= 0 && elapsed <= ReArm.CONFIG.sword.criticalCounterTimeframe.get();
    }

    public static void removePlayerCounterConditionData(UUID playerUUID) {
        counterStartTimes.removeLong(playerUUID);
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
