package me.pajic.rearm.item;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

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

    public static void initItems() {
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_bow"),
                NETHERITE_BOW
        );
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_crossbow"),
                NETHERITE_CROSSBOW
        );
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_shield"),
                NETHERITE_SHIELD
        );

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(contents -> {
            contents.addAfter(Items.CROSSBOW, NETHERITE_CROSSBOW);
            contents.addAfter(Items.BOW, NETHERITE_BOW);
            contents.addAfter(Items.SHIELD, NETHERITE_SHIELD);
        });
    }
}
