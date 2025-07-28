package me.pajic.rearm;

import net.neoforged.fml.ModList;

import java.util.List;

public class CompatFlags {
    public static final List<String> ENCHANTMENT_DESCRIPTION_MODS = List.of(
            "enchdesc",
            "idwtialsimmoedm"
    );
    public static boolean HMI_LOADED = ModList.get().isLoaded("holdmyitems");
    public static boolean ENCHDESC_MOD_LOADED = ENCHANTMENT_DESCRIPTION_MODS.stream().anyMatch(mod -> ModList.get().isLoaded(mod));
}
