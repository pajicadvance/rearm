package me.pajic.rearm.ability;

import net.minecraft.client.Minecraft;

public class CooldownTracker {

    public static int quickstepCooldown = 0;

	public static void onClientTick(Minecraft client) {
		if (client.level != null && client.player != null && !client.isPaused()) {
			if (quickstepCooldown > 0) quickstepCooldown--;
		}
	}
}
