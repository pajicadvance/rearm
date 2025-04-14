package me.pajic.rearm;

import me.pajic.rearm.ability.BackstepAbility;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.config.ReArmConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ResourceModifications;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static final ReArmConfig CONFIG = ReArmConfig.createAndLoad();

    @Override
    public void onInitialize() {
        ReArmData.init();
        ResourceModifications.init();
        ReArmEffects.init();
        ReArmItems.initItems();
        CriticalCounterAbility.init();
        BackstepAbility.init();
        CripplingThrowAbility.init();
    }
}
