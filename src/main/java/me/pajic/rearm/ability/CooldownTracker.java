package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;

public class CooldownTracker {

    public static int ABILITY_CD = Main.CONFIG.abilities.abilityCooldown();
    public static int BACKSTEP_CD = Main.CONFIG.bow.backstepTimeframe();
    public static boolean ABILITY_USED = true;

    public static boolean shouldRenderAbilityCooldownInHotbar(ItemStack stack, LocalPlayer localPlayer) {
        return localPlayer != null && ABILITY_USED &&
                (Main.CONFIG.abilities.multishotAbility() && stack.getItem() instanceof BowItem ||
                Main.CONFIG.abilities.piercingShotAbility() && stack.getItem() instanceof CrossbowItem);
    }

    public static void initClientTracker() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && !client.isPaused()) {
                if (ABILITY_CD > 0 && ABILITY_USED) ABILITY_CD--;
                if (ABILITY_CD == 0) ABILITY_USED = false;
                if (BACKSTEP_CD > 0) BACKSTEP_CD--;
            }
        });
    }
}
