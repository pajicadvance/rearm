package me.pajic.rearm.platform.neoforge;

//? neoforge {
/*import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.mixson.ClientResourceModifications;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
//? if 1.21.1 {
/^import me.pajic.rearm.renderer.LegacyThrownAxeRenderer;
^///?} else {
import me.pajic.rearm.renderer.ThrownAxeRenderer;
 //?}

@EventBusSubscriber(modid = ReArm.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	private static void onClientSetup(final FMLClientSetupEvent event) {
		ClientResourceModifications.init();
		ReArm.onInitializeClient();
	}

	@SubscribeEvent
	private static void initClientResources(AddPackFindersEvent event) {
		event.addPackFinders(
				ReArm.id("resourcepacks/" + ReArm.PACK_VERSION + "/rp"),
				PackType.CLIENT_RESOURCES,
				Component.literal("ReArm " + ReArm.PACK_VERSION + " Resource Pack"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
	}

	@SubscribeEvent
	private static void onClientTick(ClientTickEvent.Post event) {
		Minecraft client = Minecraft.getInstance();
		CooldownTracker.onClientTick(client);
		ReArmKeybinds.onClientTick(client);
	}

	@SubscribeEvent
	private static void initKeybinds(RegisterKeyMappingsEvent event) {
		event.register(ReArmKeybinds.ACTION_KEY);
	}

	@SubscribeEvent
	private static void initThrownAxeRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CripplingThrowAbility.AXE, /^? if 1.21.1 {^//^LegacyThrownAxeRenderer::new^//^?} else {^/ThrownAxeRenderer::new/^?}^/);
	}
}
*///?}
