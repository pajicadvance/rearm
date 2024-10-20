package me.pajic.rearm;

import me.pajic.rearm.ability.AbilityNetworking;
import me.pajic.rearm.config.ReArmConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static final ReArmConfig CONFIG = ReArmConfig.createAndLoad();

    @Override
    public void onInitialize() {
        ReArmEffects.initEffects();
        ReArmData.initData();
        ReArmItems.initItems();
        AbilityNetworking.initServer();
    }
}
