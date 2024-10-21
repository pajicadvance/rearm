package me.pajic.rearm.mixin;

import me.pajic.rearm.Main;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TippedArrowRecipe.class)
public class TippedArrowRecipeMixin {

    @ModifyArg(
            method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 0
            )
    )
    private Item setRequiredPotionType1(Item item) {
        return setRequiredPotionType(item);
    }

    @ModifyArg(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private Item setRequiredPotionType2(Item item) {
        return setRequiredPotionType(item);
    }

    @Unique
    private Item setRequiredPotionType(Item item) {
        if (Main.CONFIG.tweaks.craftTippedArrowsWithRegularPotions()) {
            return Items.POTION;
        }
        return item;
    }
}
