package me.pajic.rearm.renderer;

import com.bawnorton.trimica.api.client.TrimicaClientApi;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.component.DataComponentMap;

public class TrimicaCompat {

    public static void submitTrim(
            ModelPart modelPart,
            DataComponentMap map,
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            int packedLight,
            int packedOverlay,
            boolean hasFoil,
            int outlineColor
    ) {
        TrimicaClientApi.getInstance().getRenderer().submitShieldTrim(
                modelPart, map, poseStack, nodeCollector, packedLight, packedOverlay, hasFoil, outlineColor
        );
    }
}
