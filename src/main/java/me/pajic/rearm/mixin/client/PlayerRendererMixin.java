package me.pajic.rearm.mixin.client;

//? <26.1 && fabric {

/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @ModifyExpressionValue(
            method = "getArmPose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private static boolean crossbow_getArmPose(boolean original, @Local ItemStack itemStack) {
        return ReArmItems.isCrossbow(itemStack);
    }
}
*///?}
