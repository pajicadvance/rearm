package me.pajic.rearm;

import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.config.ReArmConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ResourceModifications;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("rearm")
public class Main {
    public static ReArmConfig CONFIG;

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ReArmNetworking::init);
        modEventBus.addListener(this::onInitialize);
    }

    private void registerData(RegisterEvent event) {
        ReArmEffects.init();
        ReArmItems.initItems();
        event.register(Registries.ENTITY_TYPE, registry ->
                registry.register(ResourceLocation.parse("rearm:axe"), CripplingThrowAbility.AXE));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(
                    Items.BOW.getDefaultInstance(),
                    ReArmItems.NETHERITE_BOW.value().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
            event.insertAfter(
                    Items.CROSSBOW.getDefaultInstance(),
                    ReArmItems.NETHERITE_CROSSBOW.value().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    public void onInitialize(FMLCommonSetupEvent event) {
        CONFIG = ReArmConfig.createAndLoad();
        ReArmData.init();
        ResourceModifications.init();
    }
}
