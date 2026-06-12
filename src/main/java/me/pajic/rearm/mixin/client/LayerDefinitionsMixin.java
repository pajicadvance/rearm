package me.pajic.rearm.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.object.equipment.ShieldModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {

    @ModifyReceiver(
            method = "createRoots",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
            )
    )
    private static ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> addModShieldLayer(
			ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> instance
	) {
		instance.put(RendererConstants.NETHERITE_SHIELD_LAYER, ShieldModel.createLayer());
		return instance;
    }
}
