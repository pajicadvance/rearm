package me.pajic.rearm.mixin.modern;

//? if > 1.21.1 {

import com.mojang.serialization.MapCodec;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.renderer.NetheriteShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(SpecialModelRenderers.class)
public class SpecialModelRenderersMixin {

    @Shadow @Final /*? if fabric {*/public/*?} else {*//*private*//*?}*/
	static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

    @Inject(
            method = "bootstrap",
            at = @At("TAIL")
    )
    private static void addNetheriteShieldRenderer(CallbackInfo ci) {
        ID_MAPPER.put(ReArm.id("netherite_shield"), NetheriteShieldSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
//?}
