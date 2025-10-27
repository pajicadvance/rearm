package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.mixson.ClientResourceModifications;
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
        ClientResourceModifications.init();
        ConfigApi.event().onUpdateClient((rl, config) -> {
            if (rl.equals(Main.CONFIG_RL) &&
                    Main.CONFIG.armor.helmetArmorPercent.get() +
                            Main.CONFIG.armor.chestplateArmorPercent.get() +
                            Main.CONFIG.armor.leggingsArmorPercent.get() +
                            Main.CONFIG.armor.bootsArmorPercent.get() != 100
            ) {
                Main.CONFIG.armor.helmetArmorPercent.validateAndSet(Main.CONFIG.armor.helmetArmorPercent.getDefault());
                Main.CONFIG.armor.chestplateArmorPercent.validateAndSet(Main.CONFIG.armor.chestplateArmorPercent.getDefault());
                Main.CONFIG.armor.leggingsArmorPercent.validateAndSet(Main.CONFIG.armor.leggingsArmorPercent.getDefault());
                Main.CONFIG.armor.bootsArmorPercent.validateAndSet(Main.CONFIG.armor.bootsArmorPercent.getDefault());
            }
        });
    }
}
