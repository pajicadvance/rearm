package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ClientResourceModifications;
import me.pajic.rearm.mixson.ResourceModifications;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "rearm";
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(ReArmData::registerDatapacks);
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ReArmNetworking::init);
        modEventBus.addListener(this::onInitialize);
        modEventBus.addListener(this::onInitializeClient);
    }

    private void registerData(RegisterEvent event) {
        ReArmEffects.init();
        ReArmItems.initItems();
        event.register(Registries.ENTITY_TYPE, registry ->
                registry.register(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "axe"), CripplingThrowAbility.AXE));
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
        ResourceModifications.init();
    }

    public void onInitializeClient(FMLClientSetupEvent event) {
        ClientResourceModifications.init();
    }
}
