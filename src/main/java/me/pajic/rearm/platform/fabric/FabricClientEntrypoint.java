package me.pajic.rearm.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

//? >=26.1 {
import me.pajic.rearm.hud.ItemUseProgressBars;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
*///?}

//~ if <26.1 'keymapping.v1.KeyMappingHelper' -> 'keybinding.v1.KeyBindingHelper'
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

//~ if <26.1 'ThrownAxeRenderer' -> 'LegacyThrownAxeRenderer'
import me.pajic.rearm.renderer.ThrownAxeRenderer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ReArm.onInitializeClient();
        ClientPlayNetworking.registerGlobalReceiver(
                CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE,
                (payload, context) -> CooldownTracker.counterTimerActive = true
        );
        ClientTickEvents.END_CLIENT_TICK.register(CooldownTracker::onClientTick);
        //? >=26.1
        KeyMapping.Category.register(ReArm.id("keys"));
        //~ if <26.1 'KeyMappingHelper.registerKeyMapping' -> 'KeyBindingHelper.registerKeyBinding'
        KeyMappingHelper.registerKeyMapping(ReArmKeybinds.ACTION_KEY);
        ClientTickEvents.END_CLIENT_TICK.register(ReArmKeybinds::onClientTick);
        //~ if <26.1 'ThrownAxeRenderer' -> 'LegacyThrownAxeRenderer'
        //~ if <26.1 'EntityRenderers' -> 'EntityRendererRegistry'
        EntityRenderers.register(CripplingThrowAbility.AXE, ThrownAxeRenderer::new);
        //? >=26.1 {
        ItemUseProgressBars.BARS.forEach(bar ->
                HudElementRegistry.attachElementAfter(VanillaHudElements.INFO_BAR, bar.getId(), (graphics, dt) -> bar.extractBackground(graphics))
        );
        //?}
    }
}
//?}
