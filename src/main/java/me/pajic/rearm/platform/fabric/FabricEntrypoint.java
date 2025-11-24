package me.pajic.rearm.platform.fabric;

//? fabric {

import me.pajic.rearm.CompatFlags;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.BackstepAbility;
import me.pajic.rearm.ability.BashAbility;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.mixson.CommonResourceModifications;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
//? if > 1.21.1 {
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantable;
//?}

@SuppressWarnings("unused")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		CommonResourceModifications.init();
		ReArm.onInitialize();
		ReArmItems.init();
		ReArmEffects.init();
		initCommonResources();
		initItems();
		initBackstep();
		initBash();
		initCripplingThrow();
		initCriticalCounter();
		enchantableShield();
	}

	private void initCommonResources() {
		FabricLoader.getInstance().getModContainer(ReArm.MOD_ID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(
					ReArm.id(ReArm.PACK_VERSION + "/dp"),
					modContainer,
					ResourcePackActivationType.ALWAYS_ENABLED
			);
			if (ReArm.CONFIG.bow.enableBackstep.get()) {
				ResourceManagerHelper.registerBuiltinResourcePack(
						ReArm.id("backstep"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
			}
			if (ReArm.CONFIG.axe.cripplingThrow.get()) {
				ResourceManagerHelper.registerBuiltinResourcePack(
						ReArm.id("crippling_throw"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
			}
			if (ReArm.CONFIG.protection.magicProtection.get()) {
				ResourceManagerHelper.registerBuiltinResourcePack(
						ReArm.id("magic_protection"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
			}
			if (ReArm.CONFIG.shield.enableBash.get()) {
				ResourceManagerHelper.registerBuiltinResourcePack(
						ReArm.id("bash"),
						modContainer,
						ResourcePackActivationType.ALWAYS_ENABLED
				);
			}
		});
	}

	private static void initItems() {
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
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(contents -> {
			contents.addAfter(Items.CROSSBOW, ReArmItems.NETHERITE_CROSSBOW);
			contents.addAfter(Items.BOW, ReArmItems.NETHERITE_BOW);
			contents.addAfter(Items.SHIELD, ReArmItems.NETHERITE_SHIELD);
		});
	}

	private void initBackstep() {
		PayloadTypeRegistry.playC2S().register(BackstepAbility.C2SCauseBackstepExhaustionPayload.TYPE, BackstepAbility.C2SCauseBackstepExhaustionPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(
				BackstepAbility.C2SCauseBackstepExhaustionPayload.TYPE,
				(payload, context) -> context.player().causeFoodExhaustion(payload.exhaustion())
		);
	}

	private static void initBash() {
		PayloadTypeRegistry.playC2S().register(BashAbility.C2SBashSignal.TYPE, BashAbility.C2SBashSignal.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(
				BashAbility.C2SBashSignal.TYPE,
				(payload, context) -> BashAbility.handleBash(payload, context.player())
		);
	}

	private static void initCripplingThrow() {
		Registry.register(BuiltInRegistries.ENTITY_TYPE, ReArm.id("axe"), CripplingThrowAbility.AXE);
		PayloadTypeRegistry.playC2S().register(CripplingThrowAbility.C2SUpdatePlayerRecallCondition.TYPE, CripplingThrowAbility.C2SUpdatePlayerRecallCondition.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(
				CripplingThrowAbility.C2SUpdatePlayerRecallCondition.TYPE,
				(payload, context) -> CripplingThrowAbility.recallSignals.add(payload.activePlayerUUID())
		);
	}

	private static void initCriticalCounter() {
		PayloadTypeRegistry.playC2S().register(CriticalCounterAbility.C2SUpdatePlayerCounterCondition.TYPE, CriticalCounterAbility.C2SUpdatePlayerCounterCondition.CODEC);
		PayloadTypeRegistry.playS2C().register(CriticalCounterAbility.S2CStartCriticalCounterTimer.TYPE, CriticalCounterAbility.S2CStartCriticalCounterTimer.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(CriticalCounterAbility.C2SUpdatePlayerCounterCondition.TYPE, (payload, context) ->
				CriticalCounterAbility.setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
		);
	}

	private static void enchantableShield() {
		//? if > 1.21.1 {
		if (!CompatFlags.SHIELD_LIB_LOADED) DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
				item -> item.components().has(DataComponents.BLOCKS_ATTACKS) && item instanceof ShieldItem,
				(builder, item) -> builder.set(DataComponents.ENCHANTABLE, new Enchantable(14)).build()
		));
		//?}
	}
}
//?}
