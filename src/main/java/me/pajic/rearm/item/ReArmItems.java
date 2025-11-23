package me.pajic.rearm.item;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ReArmItems {

    public static final Item NETHERITE_BOW = new NetheriteBowItem();
    public static final Item NETHERITE_CROSSBOW = new NetheriteCrossbowItem();
    public static final Item NETHERITE_SHIELD = new NetheriteShieldItem();

    public static boolean isBow(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }

    public static boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    public static boolean isRangedWeapon(ItemStack stack) {
        return isBow(stack) || isCrossbow(stack);
    }

	public static void init() {}
}
