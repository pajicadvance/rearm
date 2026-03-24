package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.mixson.AssetPatches;
import me.pajic.rearm.platform.Platform;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import me.pajic.rearm.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.rearm.platform.neoforge.NeoforgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class ReArm {

	public static final String MOD_ID = /*$ mod_id*/ "rearm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();
	public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

	public static void onInitialize() {}

	public static void onInitializeClient() {
		ConfigApi.event().onUpdateClient((id, _) -> {
			if (id.equals(id("config")) &&
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
		AssetPatches.init();
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
