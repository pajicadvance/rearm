package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CriticalCounterManagerClient {

    public static int timer = Main.CONFIG.sword.criticalCounterTimeframe();
    public static boolean timerActive;

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(CriticalCounterManager.S2CStartCriticalCounterTimer.TYPE, (payload, context) ->
                timerActive = true
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && client.player != null && !client.isPaused()) {
                if (timer == 0) {
                    ClientPlayNetworking.send(new CriticalCounterManager.C2SUpdatePlayerCounterCondition(client.player.getUUID(), false));
                    timerActive = false;
                    timer = Main.CONFIG.sword.criticalCounterTimeframe();
                }
                if (timerActive) {
                    if (timer == Main.CONFIG.sword.criticalCounterTimeframe()) {
                        ClientPlayNetworking.send(new CriticalCounterManager.C2SUpdatePlayerCounterCondition(client.player.getUUID(), true));
                    }
                    timer--;
                }
            }
        });
    }
}
