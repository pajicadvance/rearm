package me.pajic.rearm.platform.fabric;

//? fabric {

import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.mixson.ClientResourceModifications;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
//? if 1.21.1 {
/*import me.pajic.rearm.renderer.LegacyThrownAxeRenderer;
*///?} else {
import me.pajic.rearm.renderer.ThrownAxeRenderer;
//?}

@SuppressWarnings("unused")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientResourceModifications.init();
		ReArm.onInitializeClient();
		initClientResources();
		initCooldownTracker();
		initKeybinds();
		initThrownAxeRenderer();
	}

	private void initClientResources() {
		FabricLoader.getInstance().getModContainer(ReArm.MOD_ID).ifPresent(modContainer ->
				ResourceManagerHelper.registerBuiltinResourcePack(
						ReArm.id(ReArm.PACK_VERSION + "/rp"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				)
		);
	}

	private static void initCooldownTracker() {
		ClientPlayNetworking.registerGlobalReceiver(
				CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE,
				(payload, context) -> CooldownTracker.counterTimerActive = true
		);
		ClientTickEvents.END_CLIENT_TICK.register(CooldownTracker::onClientTick);
	}

	private static void initKeybinds() {
		KeyBindingHelper.registerKeyBinding(ReArmKeybinds.ACTION_KEY);
		ClientTickEvents.END_CLIENT_TICK.register(ReArmKeybinds::onClientTick);
	}

	//? if > 1.21.1
	@SuppressWarnings("deprecation")
	private static void initThrownAxeRenderer() {
		EntityRendererRegistry.register(CripplingThrowAbility.AXE, /*? if 1.21.1 {*//*LegacyThrownAxeRenderer::new*//*?} else {*/ThrownAxeRenderer::new/*?}*/);
	}
}
//?}
