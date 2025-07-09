package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.ability.BackstepAbility;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ResourceModifications;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Main implements ModInitializer {
    public static final String MOD_ID = "rearm";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

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
