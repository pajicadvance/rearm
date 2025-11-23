package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {

    @WrapOperation(
            method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean matches_extendShieldCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.is(ReArmItems.NETHERITE_SHIELD);
    }

    @WrapOperation(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean assemble_extendShieldCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.is(ReArmItems.NETHERITE_SHIELD);
    }
}
