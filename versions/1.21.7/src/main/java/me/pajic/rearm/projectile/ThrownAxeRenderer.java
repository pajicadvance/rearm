package me.pajic.rearm.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ThrownAxeRenderer extends EntityRenderer<ThrownAxe, ThrownAxeRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ThrownAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public void render(ThrownAxeRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(1.3F, 1.3F, 1.3F);
        poseStack.mulPose(Axis.YN.rotationDegrees(90.0F - renderState.yRot));
        if (!renderState.stuck) {
            poseStack.mulPose(Axis.ZN.rotationDegrees(Mth.lerp(renderState.partialTick, renderState.yRot, renderState.yRot + 180.0F)));
        } else {
            poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
        }
        renderState.item.render(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    public @NotNull ThrownAxeRenderState createRenderState() {
        return new ThrownAxeRenderState();
    }

    @Override
    public void extractRenderState(ThrownAxe entity, ThrownAxeRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.yRot = entity.getYRot(partialTick);
        renderState.xRot = entity.getXRot(partialTick);
        renderState.stuck = entity.getEntityData().get(ThrownAxe.STUCK);
        renderState.axe = entity.getEntityData().get(ThrownAxe.THROWN_AXE_ITEM_STACK);
        renderState.partialTick = partialTick;
        renderState.extractItemGroupRenderState(entity, itemModelResolver);
    }
}
