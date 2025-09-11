package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.ability.BackstepAbility;
import me.pajic.rearm.ability.BashAbility;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.data.ReArmData;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.ResourceModifications;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
//? if >= 1.21.7 {
/*import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
*///?}

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
        BashAbility.init();
        //? if >= 1.21.7 {
        /*if (!CompatFlags.SHIELD_LIB_LOADED) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
                item -> item.components().has(DataComponents.BLOCKS_ATTACKS) && item instanceof ShieldItem,
                (builder, item) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(14)).build()
        ));
        *///?}
    }
}
