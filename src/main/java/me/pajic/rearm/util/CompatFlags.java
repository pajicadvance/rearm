package me.pajic.rearm.util;

import me.pajic.rearm.ReArm;

import java.util.Map;

public class CompatFlags {

    public static final Map<String, String> ENCHANTMENT_DESCRIPTION_MODS = Map.of(
            "enchdesc", "desc",
            "idwtialsimmoedm", "desc",
            "item-descriptions", "description"
    );
    public static boolean TRIMICA_LOADED = ReArm.xplat().isModLoaded("trimica");
    public static boolean HMI_LOADED = ReArm.xplat().isModLoaded("hold-my-items");
    public static boolean ENCHDESC_MOD_LOADED = ENCHANTMENT_DESCRIPTION_MODS.keySet().stream().anyMatch(mod -> ReArm.xplat().isModLoaded(mod));
	public static boolean APOTHEOSIS_LOADED = ReArm.xplat().isModLoaded("apothic_attributes");

    public static boolean armorRebalanceActive() {
        return ReArm.CONFIG.armor.armorRebalance.get() && !APOTHEOSIS_LOADED;
    }
}
