package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(
			method = "evaluateWhichHandsToRender",
			at = @At("STORE"),
			name = "holdsBow"
	)
    private static boolean bow_evaluateWhichHandsToRender(
            boolean bl,
            @Local(name = "mainHandItem") ItemStack mainHandItem,
            @Local(name = "offhandItem") ItemStack offhandItem
    ) {
        return bl || ReArmItems.isBow(mainHandItem) || ReArmItems.isBow(offhandItem);
    }

    @ModifyVariable(
			method = "evaluateWhichHandsToRender",
			at = @At("STORE"),
			name = "holdsCrossbow"
	)
    private static boolean crossbow_evaluateWhichHandsToRender(
            boolean bl,
            @Local(name = "mainHandItem") ItemStack mainHandItem,
            @Local(name = "offhandItem") ItemStack offhandItem
    ) {
        return bl || ReArmItems.isCrossbow(mainHandItem) || ReArmItems.isCrossbow(offhandItem);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 0
            )
    )
    private static boolean bow_selectionUsingItemWhileHoldingBowLike(
			boolean original,
			@Local(name = "usedItemStack") ItemStack usedItemStack
	) {
        return original || ReArmItems.isBow(usedItemStack);
    }

    @ModifyExpressionValue(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
                    ordinal = 1
            )
    )
    private static boolean crossbow_selectionUsingItemWhileHoldingBowLike(
			boolean original,
			@Local(name = "usedItemStack") ItemStack usedItemStack
	) {
        return original || ReArmItems.isCrossbow(usedItemStack);
    }

    @ModifyExpressionValue(
            method = "isChargedCrossbow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private static boolean crossbow_isChargedCrossbow(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }

	//? if fabric {
    @ModifyExpressionValue(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private boolean crossbow_renderArmWithItem(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        return original || ReArmItems.isCrossbow(itemStack);
    }
	//?}
}
