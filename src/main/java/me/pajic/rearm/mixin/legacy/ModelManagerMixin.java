package me.pajic.rearm.mixin.legacy;

//? if 1.21.1 {
/*
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ModelManager.class)
public class ModelManagerMixin {

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/resources/model/ModelManager;VANILLA_ATLASES:Ljava/util/Map;"
            )
    )
    private Map<ResourceLocation, ResourceLocation> addModAtlas(Map<ResourceLocation, ResourceLocation> original) {
        Map<ResourceLocation, ResourceLocation> map = new HashMap<>(original);
        map.put(RendererConstants.NETHERITE_SHIELD_SHEET, RendererConstants.NETHERITE_SHIELD_PATTERNS);
        return map;
    }
}
*///?}
