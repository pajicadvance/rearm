package me.pajic.rearm;

import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class CompatFlags {
    public static final List<String> ENCHANTMENT_DESCRIPTION_MODS = List.of(
            "enchdesc",
            "idwtialsimmoedm"
    );
    public static boolean HMI_LOADED = FabricLoader.getInstance().isModLoaded("hold-my-items");
    public static boolean SHIELD_LIB_LOADED = FabricLoader.getInstance().isModLoaded("fabricshieldlib");
    public static boolean ENCHDESC_MOD_LOADED = ENCHANTMENT_DESCRIPTION_MODS.stream().anyMatch(mod -> FabricLoader.getInstance().isModLoaded(mod));
}
