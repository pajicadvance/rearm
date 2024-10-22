package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.item.ItemStack;

public class CooldownTracker {

    public static int ABILITY_CD = Main.CONFIG.abilities.abilityCooldown();
    public static int BACKSTEP_CD = Main.CONFIG.bow.backstepTimeframe();
    public static boolean ABILITY_USED = true;
    public static ItemStack ACTIVE_ITEM_CLIENT;
    public static AbilityType ABILITY_TYPE = AbilityType.NONE;

    public static void initClientTracker() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !client.isPaused()) {
                if (ABILITY_CD > 0 && ABILITY_USED) ABILITY_CD--;
                if (ABILITY_CD == 0) {
                    ABILITY_USED = false;
                    ACTIVE_ITEM_CLIENT = ItemStack.EMPTY;
                }
                if (BACKSTEP_CD > 0) BACKSTEP_CD--;
            }
        });
    }
}
