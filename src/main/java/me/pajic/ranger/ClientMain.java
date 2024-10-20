package me.pajic.ranger;

import me.pajic.ranger.ability.AbilityNetworking;
import me.pajic.ranger.ability.CooldownTracker;
import me.pajic.ranger.keybind.RangerKeybinds;
import me.pajic.ranger.model.RangerModels;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RangerModels.initModels();
        RangerKeybinds.initKeybinds();
        CooldownTracker.initClientTracker();
        AbilityNetworking.initClient();
    }
}
