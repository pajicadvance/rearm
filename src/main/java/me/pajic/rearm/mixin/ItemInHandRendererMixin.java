package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Definition(id = "CROSSBOW", field = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(CROSSBOW)")
    @ModifyExpressionValue(
            method = "evaluateWhichHandsToRender",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean crossbow_evaluateWhichHandsToRender(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
        return original || isModCrossbow(itemStack);
    }

    @Definition(id = "CROSSBOW", field = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(CROSSBOW)")
    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean crossbow_selectionUsingItemWhileHoldingBowLike(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
        return original || isModCrossbow(itemStack);
    }

    @Definition(id = "CROSSBOW", field = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(CROSSBOW)")
    @ModifyExpressionValue(
            method = "isChargedCrossbow",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean crossbow_isChargedCrossbow(boolean original, @Local(ordinal = 0, argsOnly = true) ItemStack itemStack) {
        return original || isModCrossbow(itemStack);
    }

    @Definition(id = "CROSSBOW", field = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(CROSSBOW)")
    @ModifyExpressionValue(
            method = "renderArmWithItem",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean crossbow_renderArmWithItem(boolean original, @Local(ordinal = 0, argsOnly = true) ItemStack itemStack) {
        return original || isModCrossbow(itemStack);
    }

    @Definition(id = "BOW", field = "Lnet/minecraft/world/item/Items;BOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(BOW)")
    @ModifyExpressionValue(
            method = "evaluateWhichHandsToRender",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean bow_evaluateWhichHandsToRender(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
        return original || isModBow(itemStack);
    }

    @Definition(id = "BOW", field = "Lnet/minecraft/world/item/Items;BOW:Lnet/minecraft/world/item/Item;")
    @Expression("?.?(BOW)")
    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static boolean bow_selectionUsingItemWhileHoldingBowLike(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
        return original || isModBow(itemStack);
    }

    @Unique
    private static boolean isModCrossbow(ItemStack stack) {
        return stack.is(ReArmItems.NETHERITE_CROSSBOW);
    }

    @Unique
    private static boolean isModBow(ItemStack stack) {
        return stack.is(ReArmItems.NETHERITE_BOW);
    }
}
