package me.pajic.rearm;

import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.rearm.config.ModConfig;
import me.pajic.rearm.datapack.ModDatapacks;
import me.pajic.rearm.mixson.AssetPatches;
import me.pajic.rearm.mixson.DataPatches;
import me.pajic.rearm.mixson.MixsonHelper;
import me.pajic.rearm.platform.MultiLoaderUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? <26.1
//import me.pajic.rearm.renderer.ReArmModels;

public class ReArm {

    public static final String MOD_ID = /*$ mod_id*/ "rearm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
        ModDatapacks.init();
        MixsonHelper.setDebugFlags();
        DataPatches.init();
    }

    public static void onInitializeClient() {
        ConfigApi.event().onUpdateClient((id, c) -> {
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
        //? <26.1
        //ReArmModels.initModels();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
