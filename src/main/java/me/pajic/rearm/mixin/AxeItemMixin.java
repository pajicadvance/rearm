package me.pajic.rearm.mixin;

//? <26.1 {

/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.util.ModTags;import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @ModifyExpressionValue(
            method = "playerHasShieldUseIntent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean extendShieldCheck(boolean original, @Local(argsOnly = true) UseOnContext context) {
        return original || (context.getPlayer() != null && context.getPlayer().getOffhandItem().is(ModTags.SHIELDS));
    }
}
*///?}
