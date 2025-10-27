package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ResourceModifications;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if > 1.21.1 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
*///?}

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "rearm";
    private static final Logger LOGGER = LoggerFactory.getLogger("ReArm");
    private static final boolean DEBUG = !FMLLoader/*? if > 1.21.1 {*//*.getCurrent()*//*?}*/.isProduction();
    public static final ResourceLocation CONFIG_RL = ResourceLocation.fromNamespaceAndPath(MOD_ID, "config");
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
    public static final TagKey<Item> SHIELDS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "tools/shield")
    );

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(ReArmData::registerDatapacks);
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::addCreative);
        //? if > 1.21.1
        /*modEventBus.addListener(this::modifyComponents);*/
        modEventBus.addListener(ReArmNetworking::init);
        modEventBus.addListener(this::onInitialize);
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
            event.insertAfter(
                    Items.SHIELD.getDefaultInstance(),
                    ReArmItems.NETHERITE_SHIELD.value().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    //? if > 1.21.1 {
    /*private void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modifyMatching(
                item -> item.components().has(DataComponents.BLOCKS_ATTACKS) && item instanceof ShieldItem,
                builder -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(14)).build()
        );
    }
    *///?}

    public void onInitialize(FMLCommonSetupEvent event) {
        ResourceModifications.init();
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
