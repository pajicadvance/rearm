package me.pajic.ranger;

import me.pajic.ranger.config.RangerConfig;
import me.pajic.ranger.data.RangerData;
import me.pajic.ranger.effect.BackstepEffect;
import me.pajic.ranger.item.RangerItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class Main implements ModInitializer {

    public static final RangerConfig CONFIG = RangerConfig.createAndLoad();

    @SuppressWarnings("unused")
    public static final MobEffect BACKSTEP_EFFECT = Registry.register(
            BuiltInRegistries.MOB_EFFECT,
            ResourceLocation.fromNamespaceAndPath("ranger", "backstep_effect"),
            new BackstepEffect()
    );

    @Override
    public void onInitialize() {
        RangerData.initData();
        RangerItems.initItems();
    }
}
