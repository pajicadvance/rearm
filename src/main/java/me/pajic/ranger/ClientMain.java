package me.pajic.ranger;

import me.pajic.ranger.keybind.RangerKeybinds;
import me.pajic.ranger.model.RangerModels;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RangerModels.initModels();
        RangerKeybinds.initKeybinds();
    }
}
