package me.pajic.rearm.mixin.client;

//? >=26.3 && fabric {

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {

    @ModifyExpressionValue(
            method = {"submitArmWithItem"},
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
}
//?}
