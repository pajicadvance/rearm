package me.pajic.rearm.item;

import me.pajic.rearm.ReArm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class NetheriteBowItem extends BowItem {

    public NetheriteBowItem() {
        super(
                new Properties().fireResistant().stacksTo(1).durability(576)
                .repairable(Items.NETHERITE_INGOT).enchantable(15)
                .setId(ResourceKey.create(Registries.ITEM, ReArm.id("netherite_bow")))
        );
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ReArm.CONFIG.bow.bowNetheriteVariant.get();
    }
}
