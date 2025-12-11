package me.pajic.rearm.mixin.modern;

//? if > 1.21.1 {

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.resources.model.Material;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
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

	//? if > 1.21.10 {
	/**
	 * TODO Probably remove this in a future version
	 * Temporary workaround to disable the duplicate atlas warning.
	 */
	@WrapWithCondition(
			//? if fabric
			method = "method_76672",
			//? if neoforge
			//method = "lambda$updateSpriteMaps$6",
			at = @At(
					value = "INVOKE",
					target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"
			)
	)
	private static boolean disableDuplicateAtlasWarning(
			Logger instance, String s, Object[] objects,
			@Local(ordinal = 1) TextureAtlasSprite textureAtlasSprite2,
			@Local(argsOnly = true) Material material
	) {
		return !material.atlasLocation().getNamespace().equals(ReArm.MOD_ID) && !textureAtlasSprite2.atlasLocation().getNamespace().equals(ReArm.MOD_ID);
	}
	//?}
}
//?}
