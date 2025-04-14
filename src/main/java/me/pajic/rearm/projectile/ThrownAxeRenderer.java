package me.pajic.rearm.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pajic.rearm.ability.CripplingThrowAbility;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
public class ThrownAxeRenderer extends EntityRenderer<ThrownAxe> {
    private final ItemRenderer itemRenderer;

    public ThrownAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull ThrownAxe entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.3F, 1.3F, 1.3F);
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F - entityYaw));
        if (!entity.getEntityData().get(ThrownAxe.STUCK)) {
            poseStack.mulPose(Axis.ZN.rotationDegrees(Mth.lerp(partialTick, entityYaw, entityYaw + 180.0F)));
        } else {
            poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
        }
        itemRenderer.renderStatic(
                (LivingEntity) entity.getOwner(),
                entity.getAttachedOrElse(CripplingThrowAbility.THROWN_AXE_ITEM_STACK, new ItemStack(Items.DIAMOND_AXE)).copy(),
                ItemDisplayContext.FIXED, false, poseStack, bufferSource,
                entity.level(), packedLight, OverlayTexture.NO_OVERLAY, 0
        );
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownAxe entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
