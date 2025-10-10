package me.pajic.rearm;

import net.neoforged.fml.ModList;

import java.util.Map;

public class CompatFlags {
    public static final Map<String, String> ENCHANTMENT_DESCRIPTION_MODS = Map.of(
            "enchdesc", "desc",
            "idwtialsimmoedm", "desc",
            "item_descriptions", "description"
    );
    public static boolean HMI_LOADED = ModList.get().isLoaded("holdmyitems");
    public static boolean SHIELD_LIB_LOADED = ModList.get().isLoaded("fabricshieldlib") || ModList.get().isLoaded("shieldlib");
    public static boolean ENCHDESC_MOD_LOADED = ENCHANTMENT_DESCRIPTION_MODS.keySet().stream().anyMatch(mod -> ModList.get().isLoaded(mod));
}
