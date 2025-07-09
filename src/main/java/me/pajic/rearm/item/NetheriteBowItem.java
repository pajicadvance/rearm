package me.pajic.rearm.item;

import me.pajic.rearm.Main;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
//? if 1.21.7 {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.Repairable;
*///?}

public class NetheriteBowItem extends BowItem {
    public NetheriteBowItem() {
        super(
                new Properties().fireResistant().stacksTo(1).durability(576)
                        //? if 1.21.7
                        /*.repairable(Items.NETHERITE_INGOT).enchantable(15)*/
        );
    }

    //? if 1.21.1 {
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate) {
        return Ingredient.of(Items.NETHERITE_INGOT).test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }
    //?}

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return Main.CONFIG.bow.bowNetheriteVariant.get();
    }
}
