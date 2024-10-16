package me.pajic.ranger.item;

import me.pajic.ranger.Main;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class NetheriteCrossbowItem extends CrossbowItem {
    public NetheriteCrossbowItem() {
        super(new Properties().fireResistant().stacksTo(1).durability(2031).component(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY));
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate) {
        return Ingredient.of(Items.NETHERITE_INGOT).test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return Main.CONFIG.crossbow.crossbowNetheriteVariant();
    }
}
