package me.pajic.rearm.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NetheriteShieldSpecialRenderer extends ShieldSpecialRenderer {

    private final SpriteGetter sprites;
    private final ShieldModel model;

    public NetheriteShieldSpecialRenderer(SpriteGetter sprites, ShieldModel model) {
        super(sprites, model);
        this.sprites = sprites;
        this.model = model;
    }

	@Override
    public void submit(
            @Nullable DataComponentMap components,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor
    ) {
		BannerPatternLayers patterns = components != null
				? components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
				: BannerPatternLayers.EMPTY;
		DyeColor baseColor = components != null ? components.get(DataComponents.BASE_COLOR) : null;
		boolean hasPatterns = !patterns.layers().isEmpty() || baseColor != null;
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        SpriteId base = hasPatterns ? RendererConstants.NETHERITE_SHIELD_BASE : RendererConstants.NO_PATTERN_NETHERITE_SHIELD;
		submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, base, this.sprites, outlineColor, null);
		if (hasPatterns) {
			BannerRenderer.submitPatterns(
					this.sprites,
					poseStack,
					submitNodeCollector,
					lightCoords,
					overlayCoords,
					this.model,
					Unit.INSTANCE,
					false,
					Objects.requireNonNullElse(baseColor, DyeColor.WHITE),
					patterns,
					null
			);
		}
		if (hasFoil) {
			submitNodeCollector.submitModel(
					this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, this.sprites.get(base), 0, null
			);
		}
        /*if (CompatFlags.TRIMICA_LOADED) TrimicaCompat.submitTrim(
                model.plate(), dataComponentMap, poseStack, nodeCollector, packedLight, packedOverlay, hasFoil, outlineColor
        );*/

        poseStack.popPose();
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override @NotNull
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<DataComponentMap> bake(BakingContext context) {
            return new NetheriteShieldSpecialRenderer(
                    context.sprites(),
                    new ShieldModel(context.entityModelSet().bakeLayer(RendererConstants.NETHERITE_SHIELD_LAYER))
            );
        }
    }
}
