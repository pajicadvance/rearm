package me.pajic.rearm.mixin.legacy;

//? if 1.21.1 {
/*
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

    @ModifyExpressionValue(
            method = "renderByItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 0
            )
    )
    private boolean extendShieldCheck(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || stack.is(ReArmItems.NETHERITE_SHIELD);
    }

    @ModifyExpressionValue(
            method = "renderByItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/resources/model/ModelBakery;SHIELD_BASE:Lnet/minecraft/client/resources/model/Material;"
            )
    )
    private Material handleModShieldBase(Material original, @Local(argsOnly = true) ItemStack stack) {
        return stack.is(ReArmItems.NETHERITE_SHIELD) ? RendererConstants.NETHERITE_SHIELD_BASE : original;
    }

    @ModifyExpressionValue(
            method = "renderByItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/resources/model/ModelBakery;NO_PATTERN_SHIELD:Lnet/minecraft/client/resources/model/Material;"
            )
    )
    private Material handleModShieldNoPattern(Material original, @Local(argsOnly = true) ItemStack stack) {
        return stack.is(ReArmItems.NETHERITE_SHIELD) ? RendererConstants.NO_PATTERN_NETHERITE_SHIELD : original;
    }
}
*///?}
