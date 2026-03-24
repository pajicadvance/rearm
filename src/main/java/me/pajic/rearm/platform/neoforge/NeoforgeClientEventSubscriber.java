package me.pajic.rearm.platform.neoforge;

//? neoforge {

/*import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.renderer.ThrownAxeRenderer;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = ReArm.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLCommonSetupEvent event) {
		ReArm.onInitializeClient();
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
		event.registerEntityRenderer(CripplingThrowAbility.AXE, ThrownAxeRenderer::new);
	}
}
*///?}
