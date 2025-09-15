package me.pajic.rearm.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import me.pajic.rearm.Main;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NetheriteShieldSpecialRenderer extends ShieldSpecialRenderer {
    private final ShieldModel model;

    public NetheriteShieldSpecialRenderer(ShieldModel model) {
        super(model);
        this.model = model;
    }

    @Override
    public void render(@Nullable DataComponentMap dataComponentMap, @NotNull ItemDisplayContext itemDisplayContext, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int j, boolean bl) {
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
        VertexConsumer vertexConsumer = material.sprite()
                .wrap(ItemRenderer.getFoilBuffer(multiBufferSource, this.model.renderType(material.atlasLocation()), itemDisplayContext == ItemDisplayContext.GUI, bl));
        this.model.handle().render(poseStack, vertexConsumer, i, j);
        if (bl2) {
            BannerRenderer.renderPatterns(
                    poseStack,
                    multiBufferSource,
                    i,
                    j,
                    this.model.plate(),
                    material,
                    false,
                    Objects.requireNonNullElse(dyeColor, DyeColor.WHITE),
                    bannerPatternLayers,
                    bl,
                    false
            );
        } else {
            this.model.plate().render(poseStack, vertexConsumer, i, j);
        }

        poseStack.popPose();
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final Unbaked INSTANCE = new Unbaked();
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

        @Override @NotNull
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new NetheriteShieldSpecialRenderer(new ShieldModel(modelSet.bakeLayer(RendererConstants.NETHERITE_SHIELD_LAYER)));
        }
    }
}
