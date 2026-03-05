package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.platform.Platform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//? if 1.21.1
//import me.pajic.rearm.model.ReArmModels;

//? fabric {
import me.pajic.rearm.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.rearm.platform.neoforge.NeoforgePlatform;
*///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ReArm {

	public static final String MOD_ID = /*$ mod_id*/ "rearm";
	public static final String MOD_VERSION = /*$ mod_version*/ "2.4.16";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "ReArm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier CONFIG_RL = Identifier.fromNamespaceAndPath(MOD_ID, "config");
	public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);
	private static final Platform PLATFORM = createPlatformInstance();
	public static final String PACK_VERSION = PLATFORM.mcVersion().replace(".", "_");

	public static final TagKey<Item> SHIELDS = TagKey.create(
			Registries.ITEM,
			Identifier.fromNamespaceAndPath("c", "tools/shield")
	);

	public static void onInitialize() {
	}

	public static void onInitializeClient() {
		//? if 1.21.1
		//ReArmModels.initModels();
		ConfigApi.event().onUpdateClient((rl, config) -> {
			if (rl.equals(ReArm.CONFIG_RL) &&
					ReArm.CONFIG.armor.helmetArmorPercent.get() +
							ReArm.CONFIG.armor.chestplateArmorPercent.get() +
							ReArm.CONFIG.armor.leggingsArmorPercent.get() +
							ReArm.CONFIG.armor.bootsArmorPercent.get() != 100
			) {
				ReArm.CONFIG.armor.helmetArmorPercent.validateAndSet(ReArm.CONFIG.armor.helmetArmorPercent.getDefault());
				ReArm.CONFIG.armor.chestplateArmorPercent.validateAndSet(ReArm.CONFIG.armor.chestplateArmorPercent.getDefault());
				ReArm.CONFIG.armor.leggingsArmorPercent.validateAndSet(ReArm.CONFIG.armor.leggingsArmorPercent.getDefault());
				ReArm.CONFIG.armor.bootsArmorPercent.validateAndSet(ReArm.CONFIG.armor.bootsArmorPercent.getDefault());
			}
		});
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		*///?}
	}

	public static boolean armorRebalanceActive() {
		return CONFIG.armor.armorRebalance.get() && !CompatFlags.APOTHEOSIS_LOADED;
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void debugLog(String message, Object ... args) {
		if (PLATFORM.isDebug()) LOGGER.info(message, args);
	}
}
