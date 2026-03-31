package me.pajic.rearm.platform.neoforge;

//? neoforge {

/*import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.BackstepAbility;
import me.pajic.rearm.ability.BashAbility;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(ReArm.MOD_ID)
@EventBusSubscriber(modid = ReArm.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
		ReArm.onInitialize();
	}

	@SubscribeEvent
	private static void initCommonResources(AddPackFindersEvent event) {
		if (ReArm.CONFIG.bow.enableBackstep.get()) event.addPackFinders(
				ReArm.id("resourcepacks/backstep"),
				PackType.SERVER_DATA,
				Component.literal("ReArm Backstep"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
		if (ReArm.CONFIG.axe.cripplingThrow.get()) event.addPackFinders(
				ReArm.id("resourcepacks/crippling_throw"),
				PackType.SERVER_DATA,
				Component.literal("ReArm Crippling Throw"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
		if (ReArm.CONFIG.protection.magicProtection.get()) event.addPackFinders(
				ReArm.id("resourcepacks/magic_protection"),
				PackType.SERVER_DATA,
				Component.literal("ReArm Magic Protection"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
		if (ReArm.CONFIG.shield.enableBash.get()) event.addPackFinders(
				ReArm.id("resourcepacks/bash"),
				PackType.SERVER_DATA,
				Component.literal("ReArm Bash"),
				PackSource.BUILT_IN,
				true,
				Pack.Position.TOP
		);
	}

	@SubscribeEvent
	private static void initNetworkEvents(RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(
				BackstepAbility.C2SCauseBackstepExhaustionPayload.TYPE,
				BackstepAbility.C2SCauseBackstepExhaustionPayload.CODEC,
				(payload, context) ->
						context.player().causeFoodExhaustion(payload.exhaustion())
		);
		registrar.playToServer(
				CriticalCounterAbility.C2SUpdatePlayerCounterCondition.TYPE,
				CriticalCounterAbility.C2SUpdatePlayerCounterCondition.CODEC,
				(payload, _) ->
						CriticalCounterAbility.setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
		);
		registrar.playToServer(
				CripplingThrowAbility.C2SUpdatePlayerRecallCondition.TYPE,
				CripplingThrowAbility.C2SUpdatePlayerRecallCondition.CODEC,
				(payload, _) ->
						CripplingThrowAbility.recallSignals.add(payload.activePlayerUUID())
		);
		registrar.playToServer(
				BashAbility.C2SBashSignal.TYPE,
				BashAbility.C2SBashSignal.CODEC,
				(payload, context) ->
						BashAbility.handleBash( (ServerPlayer) context.player())
		);
		registrar.playToClient(
				CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE,
				CriticalCounterAbility.S2CStartCriticalCounterTimer.CODEC,
				(_, _) -> CooldownTracker.counterTimerActive = true
		);
	}

	@SubscribeEvent
	private static void registerFeatures(RegisterEvent event) {
		ReArmItems.init();
		ReArmEffects.init();
		event.register(
				Registries.ITEM,
				registry -> registry.register(ReArm.id("netherite_bow"), ReArmItems.NETHERITE_BOW)
		);
		event.register(
				Registries.ITEM,
				registry -> registry.register(ReArm.id("netherite_crossbow"), ReArmItems.NETHERITE_CROSSBOW)
		);
		event.register(
				Registries.ITEM,
				registry -> registry.register(ReArm.id("netherite_shield"), ReArmItems.NETHERITE_SHIELD)
		);
		event.register(
				Registries.ENTITY_TYPE,
				registry -> registry.register(ReArm.id("axe"), CripplingThrowAbility.AXE)
		);
	}

	@SubscribeEvent
	private static void initItemGroups(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.COMBAT) {
			event.insertAfter(
					Items.BOW.getDefaultInstance(),
					ReArmItems.NETHERITE_BOW.getDefaultInstance(),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
			);
			event.insertAfter(
					Items.CROSSBOW.getDefaultInstance(),
					ReArmItems.NETHERITE_CROSSBOW.getDefaultInstance(),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
			);
			event.insertAfter(
					Items.SHIELD.getDefaultInstance(),
					ReArmItems.NETHERITE_SHIELD.getDefaultInstance(),
					CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
			);
		}
	}

	@SubscribeEvent
	private static void enchantableShield(ModifyDefaultComponentsEvent event) {
		if (ReArm.CONFIG.shield.enchantableVanillaShield.get()) event.modifyMatching(
				(item, components) -> item instanceof ShieldItem && !components.has(DataComponents.ENCHANTABLE),
				builder -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(1)).build()
		);
	}
}
*///?}
