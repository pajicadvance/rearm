package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "rearm", value = Dist.CLIENT)
public class CooldownTracker {
    public static int backstepCooldown = 0;
    public static int counterTimer = 0;
    public static boolean counterTimerActive;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.player != null && !client.isPaused()) {
            if (backstepCooldown > 0) backstepCooldown--;
            if (counterTimer == 0) {
                PacketDistributor.sendToServer(new ReArmNetworking.C2SUpdatePlayerCounterCondition(
                        client.player.getUUID(),
                        false
                ));
                counterTimerActive = false;
                counterTimer = Main.CONFIG.sword.criticalCounterTimeframe();
            }
            if (counterTimerActive) {
                if (counterTimer == Main.CONFIG.sword.criticalCounterTimeframe()) {
                    PacketDistributor.sendToServer(new ReArmNetworking.C2SUpdatePlayerCounterCondition(
                            client.player.getUUID(),
                            true
                    ));
                }
                counterTimer--;
            }
        }
    }
}
