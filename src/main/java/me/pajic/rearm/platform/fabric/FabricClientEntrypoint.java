package me.pajic.rearm.platform.fabric;

//? fabric {

import me.pajic.rearm.ReArm;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.hud.ItemUseProgressBars;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.renderer.ThrownAxeRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.renderer.entity.EntityRenderers;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ReArm.onInitializeClient();
		initCooldownTracker();
		initKeybinds();
		initThrownAxeRenderer();
		initItemProgressBars();
	}

	private static void initCooldownTracker() {
		ClientPlayNetworking.registerGlobalReceiver(
				CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE,
				(payload, context) -> CooldownTracker.counterTimerActive = true
		);
		ClientTickEvents.END_CLIENT_TICK.register(CooldownTracker::onClientTick);
	}

	private static void initKeybinds() {
		KeyMappingHelper.registerKeyMapping(ReArmKeybinds.ACTION_KEY);
		ClientTickEvents.END_CLIENT_TICK.register(ReArmKeybinds::onClientTick);
	}

	private static void initThrownAxeRenderer() {
		EntityRenderers.register(CripplingThrowAbility.AXE, ThrownAxeRenderer::new);
	}

	private static void initItemProgressBars() {
		ItemUseProgressBars.bars.forEach(bar ->
				HudElementRegistry.attachElementAfter(VanillaHudElements.INFO_BAR, bar.getId(), bar::extractBackground)
		);
	}
}
//?}
