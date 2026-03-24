package me.pajic.rearm.item;

import me.pajic.rearm.ReArm;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.jetbrains.annotations.NotNull;

public class NetheriteCrossbowItem extends CrossbowItem {

    public NetheriteCrossbowItem() {
        super(
                new Properties().fireResistant().stacksTo(1).durability(704)
				.component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY)
				.repairable(Items.NETHERITE_INGOT).enchantable(15)
				.setId(ResourceKey.create(Registries.ITEM, ReArm.id("netherite_crossbow")))
        );
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ReArm.CONFIG.crossbow.crossbowNetheriteVariant.get();
    }
}
