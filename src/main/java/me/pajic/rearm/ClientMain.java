package me.pajic.rearm;

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
    }
}
