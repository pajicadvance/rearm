package me.pajic.rearm.item;

import me.pajic.rearm.Main;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class NetheriteBowItem extends BowItem {
    public NetheriteBowItem() {
        super(new Properties().fireResistant().stacksTo(1).durability(576));
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
        return Main.CONFIG.bow.bowNetheriteVariant.get();
    }
}
