package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.item.ItemStack;

public class CooldownTracker {

    public static int abilityCooldown = Main.CONFIG.abilities.abilityCooldown();
    public static int backstepCooldown = Main.CONFIG.bow.backstepTimeframe();
    public static boolean abilityUsed = true;
    public static ItemStack activeItemClient;
    public static AbilityType abilityType = AbilityType.NONE;

    public static void initClientTracker() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !client.isPaused()) {
                if (abilityCooldown > 0 && abilityUsed) abilityCooldown--;
                if (abilityCooldown == 0) {
                    abilityUsed = false;
                    activeItemClient = ItemStack.EMPTY;
                }
                if (backstepCooldown > 0) backstepCooldown--;
            }
        });
    }
}
