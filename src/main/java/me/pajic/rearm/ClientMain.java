package me.pajic.rearm;

import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.projectile.ThrownAxeRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = Main.MOD_ID, dist = Dist.CLIENT)
public class ClientMain {
    public ClientMain(IEventBus modEventBus) {
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(ReArmKeybinds::registerKeybinds);
        modEventBus.addListener(this::onInitialize);
    }

    private void registerData(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CripplingThrowAbility.AXE, ThrownAxeRenderer::new);
    }

    public void onInitialize(FMLClientSetupEvent event) {
        //? if 1.21.1
        ReArmModels.initModels();
    }
}
