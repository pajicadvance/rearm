package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "getFieldOfViewModifier",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private boolean checkForModdedBows(boolean original) {
        return original || ReArmItems.isBow(((AbstractClientPlayer) (Object) this).getUseItem());
    }
}
