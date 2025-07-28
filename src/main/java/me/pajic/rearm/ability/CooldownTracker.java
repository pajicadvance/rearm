package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class CooldownTracker {
    public static int backstepCooldown = Main.CONFIG.bow.backstepTimeframe.get();
    public static int counterTimer = Main.CONFIG.sword.criticalCounterTimeframe.get();
    public static boolean counterTimerActive;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.player != null && !client.isPaused()) {
            if (backstepCooldown > 0) backstepCooldown--;
            if (counterTimer == 0) {
                ReArmNetworking.sendToServer(new ReArmNetworking.C2SUpdatePlayerCounterCondition(
                        client.player.getUUID(),
                        false
                ));
                counterTimerActive = false;
                counterTimer = Main.CONFIG.sword.criticalCounterTimeframe.get();
            }
            if (counterTimerActive) {
                if (counterTimer == Main.CONFIG.sword.criticalCounterTimeframe.get()) {
                    ReArmNetworking.sendToServer(new ReArmNetworking.C2SUpdatePlayerCounterCondition(
                            client.player.getUUID(),
                            true
                    ));
                }
                counterTimer--;
            }
        }
    }
}
