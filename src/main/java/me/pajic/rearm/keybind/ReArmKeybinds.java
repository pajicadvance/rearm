package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.QuickstepAbility;
import me.pajic.rearm.ability.BashAbility;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.lwjgl.glfw.GLFW;

public class ReArmKeybinds {

    private static final KeyMapping.Category MOD_KEYS = KeyMapping.Category.register(ReArm.id("keys"));

    public static final KeyMapping ACTION_KEY = new KeyMapping(
			"key.rearm.action",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT_ALT,
			MOD_KEYS
	);

	public static void onClientTick(Minecraft client) {
		if (ACTION_KEY.isDown() && client.level != null && client.player != null) {
			if (ReArm.CONFIG.bow.enableBackstep.get() && CooldownTracker.quickstepCooldown == 0 && tryQuickstep(ACTION_KEY, client)) {
				CooldownTracker.quickstepCooldown = ReArm.CONFIG.bow.backstepTimeframe.get();
			}
			ReArm.xplat().sendToServer(new CripplingThrowAbility.C2SUpdatePlayerRecallCondition(client.player.getUUID()));
			if (ReArm.CONFIG.shield.enableBash.get()) ReArm.xplat().sendToServer(new BashAbility.C2SBashSignal());
		}
	}

	@SuppressWarnings("DataFlowIssue")
	public static boolean tryQuickstep(KeyMapping actionKey, Minecraft client) {
		LocalPlayer player = client.player;
		if (player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)) {
			QuickstepAbility.dash(player);
			ReArm.xplat().sendToServer(new QuickstepAbility.C2SQuickstepSignal());
			actionKey.setDown(false);
			return true;
		}
		return false;
	}
}
