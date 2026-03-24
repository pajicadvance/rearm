package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.objectweb.asm.Opcodes;
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
					target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;KNOWN_ATLASES:Ljava/util/List;",
					opcode = Opcodes.GETSTATIC
			)
    )
    private List<AtlasManager.AtlasConfig> addModAtlas(List<AtlasManager.AtlasConfig> original) {
        List<AtlasManager.AtlasConfig> list = new ArrayList<>(original);
        list.add(new AtlasManager.AtlasConfig(RendererConstants.NETHERITE_SHIELD_SHEET, RendererConstants.NETHERITE_SHIELD_PATTERNS, false));
        return list;
    }

	/**
	 * TODO Probably remove this in a future version
	 * Temporary workaround to disable the duplicate atlas warning.
	 */
	@WrapWithCondition(
			method = "lambda$updateSpriteMaps$0",
			at = @At(
					value = "INVOKE",
					target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"
			)
	)
	private static boolean disableDuplicateAtlasWarning(
			Logger instance, String s, Object[] objects,
			@Local(name = "id") SpriteId id,
			@Local(name = "previous") TextureAtlasSprite previous
	) {
		return !id.atlasLocation().getNamespace().equals(ReArm.MOD_ID) && !previous.atlasLocation().getNamespace().equals(ReArm.MOD_ID);
	}
}
