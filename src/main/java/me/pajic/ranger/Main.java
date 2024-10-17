package me.pajic.ranger;

import me.pajic.ranger.config.RangerConfig;
import me.pajic.ranger.data.RangerData;
import me.pajic.ranger.effect.RangerEffects;
import me.pajic.ranger.item.RangerItems;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static final RangerConfig CONFIG = RangerConfig.createAndLoad();

    @Override
    public void onInitialize() {
        RangerEffects.initEffects();
        RangerData.initData();
        RangerItems.initItems();
    }
}
