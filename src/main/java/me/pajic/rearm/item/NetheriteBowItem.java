package me.pajic.rearm.item;

import me.pajic.rearm.ReArm;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

//? <26.1 {
/*import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
*///?} else {
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//?}

public class NetheriteBowItem extends BowItem {

    public NetheriteBowItem() {
        super(
                new Properties().fireResistant().stacksTo(1).durability(576)
                //? >=26.1 {
                .repairable(Items.NETHERITE_INGOT).enchantable(15)
                .setId(ResourceKey.create(Registries.ITEM, ReArm.id("netherite_bow")))
                //?}
        );
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ReArm.CONFIG.bow.bowNetheriteVariant.get();
    }

    //? <26.1 {
    /*@Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate) {
        return Ingredient.of(Items.NETHERITE_INGOT).test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }
    *///?}
}
