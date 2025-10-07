package me.pajic.rearm.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import me.pajic.rearm.Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NetheriteShieldSpecialRenderer extends ShieldSpecialRenderer {
    private final MaterialSet materials;
    private final ShieldModel model;

    public NetheriteShieldSpecialRenderer(MaterialSet materials, ShieldModel model) {
        super(materials, model);
        this.materials = materials;
        this.model = model;
    }

    @Override
    public void submit(@Nullable DataComponentMap dataComponentMap, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        BannerPatternLayers bannerPatternLayers = dataComponentMap != null
                ? dataComponentMap.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                : BannerPatternLayers.EMPTY;
        DyeColor dyeColor = dataComponentMap != null ? dataComponentMap.get(DataComponents.BASE_COLOR) : null;
        boolean bl2 = !bannerPatternLayers.layers().isEmpty() || dyeColor != null;
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = bl2 ? new Material(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/atlas/netherite_shield_patterns.png"),
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "entity/netherite_shield_base")
        ) : new Material(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/atlas/netherite_shield_patterns.png"),
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "entity/netherite_shield_base_nopattern")
        );
        nodeCollector.submitModelPart(
                this.model.handle(),
                poseStack,
                this.model.renderType(material.atlasLocation()),
                packedLight,
                packedOverlay,
                this.materials.get(material),
                false,
                false,
                -1,
                null,
                outlineColor
        );
        if (bl2) {
            BannerRenderer.submitPatterns(
                    this.materials,
                    poseStack,
                    nodeCollector,
                    packedLight,
                    packedOverlay,
                    this.model,
                    Unit.INSTANCE,
                    material,
                    false,
                    Objects.requireNonNullElse(dyeColor, DyeColor.WHITE),
                    bannerPatternLayers,
                    hasFoil,
                    null,
                    outlineColor
            );
        } else {
            nodeCollector.submitModelPart(
                    this.model.plate(),
                    poseStack,
                    this.model.renderType(material.atlasLocation()),
                    packedLight,
                    packedOverlay,
                    this.materials.get(material),
                    false,
                    hasFoil,
                    -1,
                    null,
                    outlineColor
            );
        }

        poseStack.popPose();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final NetheriteShieldSpecialRenderer.Unbaked INSTANCE = new NetheriteShieldSpecialRenderer.Unbaked();
        public static final MapCodec<NetheriteShieldSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override @NotNull
        public MapCodec<NetheriteShieldSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public @NotNull SpecialModelRenderer<?> bake(BakingContext context) {
            return new NetheriteShieldSpecialRenderer(
                    context.materials(),
                    new ShieldModel(context.entityModelSet().bakeLayer(RendererConstants.NETHERITE_SHIELD_LAYER))
            );
        }
    }
}
