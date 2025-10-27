package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.keybind.ReArmKeybinds;
import net.fabricmc.api.ClientModInitializer;
//? if 1.21.1
import me.pajic.rearm.ReArmModels;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if 1.21.1
        ReArmModels.initModels();
        ReArmKeybinds.initKeybinds();
        CooldownTracker.init();
        CripplingThrowAbility.initClient();
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
