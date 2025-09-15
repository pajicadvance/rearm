package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {

    @ModifyExpressionValue(
            method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean matches_extendShieldCheck(boolean original, @Local(name = /*? if 1.21.1 {*/"itemStack3"/*?}*//*? if >= 1.21.7 {*//*"itemStack"*//*?}*/) ItemStack itemStack3) {
        return original || itemStack3.is(ReArmItems.NETHERITE_SHIELD);
    }

    @ModifyExpressionValue(
            method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean assemble_extendShieldCheck(boolean original, @Local(name = "itemStack3") ItemStack itemStack3) {
        return original || itemStack3.is(ReArmItems.NETHERITE_SHIELD);
    }
}
