package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CooldownTracker {

    public static int backstepCooldown = Main.CONFIG.bow.backstepTimeframe.get();
    public static int counterTimer = Main.CONFIG.sword.criticalCounterTimeframe.get();
    public static boolean counterTimerActive;

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE, (payload, context) ->
                CooldownTracker.counterTimerActive = true
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && client.player != null && !client.isPaused()) {
                if (backstepCooldown > 0) backstepCooldown--;
                if (counterTimer == 0) {
                    ClientPlayNetworking.send(new CriticalCounterAbility.C2SUpdatePlayerCounterCondition(
                            client.player.getUUID(),
                            false
                    ));
                    counterTimerActive = false;
                    counterTimer = Main.CONFIG.sword.criticalCounterTimeframe.get();
                }
                if (counterTimerActive) {
                    if (counterTimer == Main.CONFIG.sword.criticalCounterTimeframe.get()) {
                        ClientPlayNetworking.send(new CriticalCounterAbility.C2SUpdatePlayerCounterCondition(
                                client.player.getUUID(),
                                true
                        ));
                    }
                    counterTimer--;
                }
            }
        });
    }
}
