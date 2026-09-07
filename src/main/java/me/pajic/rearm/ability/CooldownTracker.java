package me.pajic.rearm.ability;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.platform.MultiLoaderUtil;
import net.minecraft.client.Minecraft;

public class CooldownTracker {

    public static int quickstepCooldown = ReArm.CONFIG.bow.backstepTimeframe.get();
    public static int counterTimer = ReArm.CONFIG.sword.criticalCounterTimeframe.get();
    public static boolean counterTimerActive;

	public static void onClientTick(Minecraft client) {
		if (client.level != null && client.player != null && !client.isPaused()) {
			if (quickstepCooldown > 0) quickstepCooldown--;
			if (counterTimer == 0) {
				MultiLoaderUtil.INSTANCE.sendToServer(new CriticalCounterAbility.C2SUpdatePlayerCounterCondition(
						client.player.getUUID(),
						false
				));
				counterTimerActive = false;
				counterTimer = ReArm.CONFIG.sword.criticalCounterTimeframe.get();
			}
			if (counterTimerActive) {
				if (counterTimer == ReArm.CONFIG.sword.criticalCounterTimeframe.get()) {
                    MultiLoaderUtil.INSTANCE.sendToServer(new CriticalCounterAbility.C2SUpdatePlayerCounterCondition(
							client.player.getUUID(),
							true
					));
				}
				counterTimer--;
			}
		}
	}
}
