package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.UUID;

public class CriticalCounterAbility {

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
}
