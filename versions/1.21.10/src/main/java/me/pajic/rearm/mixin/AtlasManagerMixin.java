package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.resources.model.AtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/resources/model/AtlasManager;KNOWN_ATLASES:Ljava/util/List;"
            )
    )
    private List<AtlasManager.AtlasConfig> addModAtlas(List<AtlasManager.AtlasConfig> original) {
        List<AtlasManager.AtlasConfig> list = new ArrayList<>(original);
        list.add(new AtlasManager.AtlasConfig(RendererConstants.NETHERITE_SHIELD_SHEET, RendererConstants.NETHERITE_SHIELD_PATTERNS, false));
        return list;
    }
}
