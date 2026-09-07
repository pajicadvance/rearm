package me.pajic.rearm.platform.neoforge;

//? neoforge {

/*import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.hud.ItemUseProgressBars;
import me.pajic.rearm.keybind.ReArmKeybinds;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

//~ if <26.1 'ThrownAxeRenderer' -> 'LegacyThrownAxeRenderer'
import me.pajic.rearm.renderer.LegacyThrownAxeRenderer;

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
        //~ if <26.1 'ThrownAxeRenderer' -> 'LegacyThrownAxeRenderer'
        event.registerEntityRenderer(CripplingThrowAbility.AXE, LegacyThrownAxeRenderer::new);
    }

    @SubscribeEvent
    private static void initHudLayers(RegisterGuiLayersEvent event) {
        ItemUseProgressBars.BARS.forEach(bar ->
                //~ if <26.1 'CONTEXTUAL_INFO_BAR' -> 'EXPERIENCE_BAR'
                event.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, bar.getId(), (graphics, dt) -> bar.extractBackground(graphics))
        );
    }
}
*///?}
