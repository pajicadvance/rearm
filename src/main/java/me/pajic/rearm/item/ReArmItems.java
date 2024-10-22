package me.pajic.rearm.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class ReArmItems {

    public static final Item NETHERITE_BOW = new NetheriteBowItem();
    public static final Item NETHERITE_CROSSBOW = new NetheriteCrossbowItem();

    public static boolean isBow(ItemStack stack) {
        return stack.getItem() instanceof BowItem;
    }

    public static boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    public static void initItems() {
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath("rearm", "netherite_bow"),
                NETHERITE_BOW
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(contents -> contents.addAfter(
                Items.BOW,
                NETHERITE_BOW
        ));

        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath("rearm", "netherite_crossbow"),
                NETHERITE_CROSSBOW
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(contents -> contents.addAfter(
                Items.CROSSBOW,
                NETHERITE_CROSSBOW
        ));
    }
}
