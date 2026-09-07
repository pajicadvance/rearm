package me.pajic.rearm.util;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.platform.MultiLoaderUtil;

import java.util.Map;

public class CompatFlags {

    public static final Map<String, String> ENCHANTMENT_DESCRIPTION_MODS = Map.of(
            "enchdesc", "desc",
            "idwtialsimmoedm", "desc",
            "item-descriptions", "description"
    );
    public static final boolean TRIMICA_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("trimica");
    public static final boolean ENCHDESC_MOD_LOADED = ENCHANTMENT_DESCRIPTION_MODS.keySet().stream().anyMatch(MultiLoaderUtil.INSTANCE::isModLoaded);
	public static final boolean APOTHEOSIS_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("apothic_attributes");
	public static final boolean PENCHANT_LOADED = MultiLoaderUtil.INSTANCE.isModLoaded("penchant");

    public static boolean armorRebalanceActive() {
        return ReArm.CONFIG.armor.armorRebalance.get() && !APOTHEOSIS_LOADED;
    }
}
