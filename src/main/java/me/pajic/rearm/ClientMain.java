package me.pajic.rearm;

import me.pajic.rearm.ability.AbilityManager;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.keybind.ReArmKeybinds;
import me.pajic.rearm.model.ReArmModels;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ReArmModels.initModels();
        ReArmKeybinds.initKeybinds();
        CooldownTracker.initClientTracker();
        AbilityManager.initClient();
    }
}
