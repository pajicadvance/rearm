package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(
            method = "evaluateWhichHandsToRender",
            at = @At("STORE"),
            ordinal = 0
    )
    private static boolean bow_evaluateWhichHandsToRender(
            boolean bl,
            @Local(ordinal = 0) ItemStack itemStack,
            @Local(ordinal = 1) ItemStack itemStack2
    ) {
        return bl || ReArmItems.isBow(itemStack) || ReArmItems.isBow(itemStack2);
    }

    @ModifyVariable(
            method = "evaluateWhichHandsToRender",
            at = @At("STORE"),
            ordinal = 1
    )
    private static boolean crossbow_evaluateWhichHandsToRender(
            boolean bl,
            @Local(ordinal = 0) ItemStack itemStack,
            @Local(ordinal = 1) ItemStack itemStack2
    ) {
        return bl || ReArmItems.isCrossbow(itemStack) || ReArmItems.isCrossbow(itemStack2);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 0
            )
    )
    private static boolean bow_selectionUsingItemWhileHoldingBowLike(boolean original, @Local ItemStack itemStack) {
        return original || ReArmItems.isBow(itemStack);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 1
            )
    )
    private static boolean crossbow_selectionUsingItemWhileHoldingBowLike(boolean original, @Local ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }

    @ModifyExpressionValue(
            method = "isChargedCrossbow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean crossbow_isChargedCrossbow(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }

    /*@ModifyExpressionValue(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 1
            )
    )
    private boolean crossbow_renderArmWithItem(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }*/
}
