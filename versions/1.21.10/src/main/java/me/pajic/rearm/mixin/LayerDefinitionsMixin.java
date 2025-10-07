package me.pajic.rearm.mixin;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {

    @Inject(
            method = "createRoots",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/properties/WoodType;values()Ljava/util/stream/Stream;"
            )
    )
    private static void addModShieldLayer(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, @Local ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        builder.put(RendererConstants.NETHERITE_SHIELD_LAYER, ShieldModel.createLayer());
    }
}
