package me.pajic.rearm.item;

import me.pajic.rearm.Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ReArmItems {

    public static final Holder<Item> NETHERITE_BOW = Registry.registerForHolder(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_bow"),
            new NetheriteBowItem()
    );
    public static final Holder<Item> NETHERITE_CROSSBOW = Registry.registerForHolder(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_crossbow"),
            new NetheriteCrossbowItem()
    );

    public static boolean isBow(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }

    public static boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    public static boolean isRangedWeapon(ItemStack stack) {
        return isBow(stack) || isCrossbow(stack);
    }

    public static void initItems() {}
}
