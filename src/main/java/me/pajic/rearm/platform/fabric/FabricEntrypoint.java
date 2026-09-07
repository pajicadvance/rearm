package me.pajic.rearm.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.BashAbility;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.ability.QuickstepAbility;
import me.pajic.rearm.datapack.ModDatapacks;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.predicate.EntityInWaterOrRainPredicate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

//? >=26.1 {
import me.pajic.rearm.compat.PenchantCompat;
import me.pajic.rearm.util.CompatFlags;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.ShieldItem;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
*///?}

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        ReArm.onInitialize();
        ReArmItems.init();
        ReArmEffects.init();
        FabricLoader.getInstance().getModContainer(ReArm.MOD_ID).ifPresent(container ->
                //~ if <26.1 'ResourceLoader.registerBuiltinPack' -> 'ResourceManagerHelper.registerBuiltinResourcePack'
                ModDatapacks.getPacks().forEach(s -> ResourceLoader.registerBuiltinPack(
                        ReArm.id(s),
                        container,
                        Component.translatable("rearm.pack." + s),
                        //~ if <26.1 'PackActivationType' -> 'ResourcePackActivationType'
                        PackActivationType.ALWAYS_ENABLED
                ))
        );
        Registry.register(
                BuiltInRegistries.ITEM,
                ReArm.id("netherite_bow"),
                ReArmItems.NETHERITE_BOW
        );
        Registry.register(
                BuiltInRegistries.ITEM,
                ReArm.id("netherite_crossbow"),
                ReArmItems.NETHERITE_CROSSBOW
        );
        Registry.register(
                BuiltInRegistries.ITEM,
                ReArm.id("netherite_shield"),
                ReArmItems.NETHERITE_SHIELD
        );
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ReArm.id("axe"), CripplingThrowAbility.AXE);
        //~ if <26.1 'insertAfter' -> 'addAfter' {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(contents -> {
            contents.insertAfter(Items.CROSSBOW, ReArmItems.NETHERITE_CROSSBOW);
            contents.insertAfter(Items.BOW, ReArmItems.NETHERITE_BOW);
            contents.insertAfter(Items.SHIELD, ReArmItems.NETHERITE_SHIELD);
        });
        //~}
        PayloadTypeRegistry.serverboundPlay().register(QuickstepAbility.C2SQuickstepSignal.TYPE, QuickstepAbility.C2SQuickstepSignal.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(BashAbility.C2SBashSignal.TYPE, BashAbility.C2SBashSignal.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CripplingThrowAbility.C2SUpdatePlayerRecallCondition.TYPE, CripplingThrowAbility.C2SUpdatePlayerRecallCondition.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CriticalCounterAbility.C2SUpdatePlayerCounterCondition.TYPE, CriticalCounterAbility.C2SUpdatePlayerCounterCondition.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE, CriticalCounterAbility.S2CStartCriticalCounterTimer.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                QuickstepAbility.C2SQuickstepSignal.TYPE,
                (payload, context) -> QuickstepAbility.handleQuickstep(context.player())
        );
        ServerPlayNetworking.registerGlobalReceiver(
                BashAbility.C2SBashSignal.TYPE,
                (payload, context) -> BashAbility.handleBash(context.player())
        );
        ServerPlayNetworking.registerGlobalReceiver(
                CripplingThrowAbility.C2SUpdatePlayerRecallCondition.TYPE,
                (payload, context) -> CripplingThrowAbility.recallSignals.add(payload.activePlayerUUID())
        );
        ServerPlayNetworking.registerGlobalReceiver(CriticalCounterAbility.C2SUpdatePlayerCounterCondition.TYPE, (payload, context) ->
                CriticalCounterAbility.setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
        );
        ServerTickEvents.END_SERVER_TICK.register(QuickstepAbility::onServerTick);
        initPredicates();
        //? >=26.1 {
        if (CompatFlags.PENCHANT_LOADED) PenchantCompat.init();
        if (ReArm.CONFIG.shield.enchantableVanillaShield.get()) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
                item -> item instanceof ShieldItem && !item.components().has(DataComponents.ENCHANTABLE),
                (builder, item) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(1)).build()
        ));
        //?}
    }

    private static void initPredicates() {
        Registry.register(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE, ReArm.id("is_in_water_or_rain"), EntityInWaterOrRainPredicate.CODEC);
    }
}
//?}
