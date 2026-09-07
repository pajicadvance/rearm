package me.pajic.rearm.mixin.client;

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
            boolean holdsBow,
            @Local(ordinal = 0) ItemStack mainHandItem,
            @Local(ordinal = 1) ItemStack offhandItem
    ) {
        return holdsBow || ReArmItems.isBow(mainHandItem) || ReArmItems.isBow(offhandItem);
    }

    @ModifyVariable(
			method = "evaluateWhichHandsToRender",
			at = @At("STORE"),
			ordinal = 1
	)
    private static boolean crossbow_evaluateWhichHandsToRender(
            boolean holdsCrossbow,
            @Local(ordinal = 0) ItemStack mainHandItem,
            @Local(ordinal = 1) ItemStack offhandItem
    ) {
        return holdsCrossbow || ReArmItems.isCrossbow(mainHandItem) || ReArmItems.isCrossbow(offhandItem);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    //~ if <26.1 '(Ljava/lang/Object;)' -> '(Lnet/minecraft/world/item/Item;)'
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 0
            )
    )
    private static boolean bow_selectionUsingItemWhileHoldingBowLike(
			boolean original,
			@Local ItemStack usedItemStack
	) {
        return original || ReArmItems.isBow(usedItemStack);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    //~ if <26.1 '(Ljava/lang/Object;)' -> '(Lnet/minecraft/world/item/Item;)'
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 1
            )
    )
    private static boolean crossbow_selectionUsingItemWhileHoldingBowLike(
			boolean original,
			@Local ItemStack usedItemStack
	) {
        return original || ReArmItems.isCrossbow(usedItemStack);
    }

    @ModifyExpressionValue(
            method = "isChargedCrossbow",
            at = @At(
                    value = "INVOKE",
                    //~ if <26.1 '(Ljava/lang/Object;)' -> '(Lnet/minecraft/world/item/Item;)'
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private static boolean crossbow_isChargedCrossbow(boolean original, @Local(argsOnly = true) ItemStack item) {
        return original || ReArmItems.isCrossbow(item);
    }

	//? if fabric {
    @ModifyExpressionValue(
            method = {"renderArmWithItem", "submitArmWithItem"},
            at = @At(
                    value = "INVOKE",
                    //~ if <26.1 '(Ljava/lang/Object;)' -> '(Lnet/minecraft/world/item/Item;)'
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
                    //? <26.1
                    //, ordinal = 1
            )
    )
    private boolean crossbow_renderArmWithItem(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }
	//?}
}
