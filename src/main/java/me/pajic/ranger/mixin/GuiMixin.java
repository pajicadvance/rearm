package me.pajic.ranger.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyExpressionValue(
            method = "renderEffects",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/effect/MobEffectInstance;showIcon()Z"
            )
    )
    private boolean hideBackstepDisplay(boolean original, @Local MobEffectInstance effect) {
        // Hide the backstep status effect icon
        // Since the backstep window is less than a second it's really not necessary
        if (effect.is(minecraft.level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).getHolderOrThrow(
                ResourceKey.create(
                        Registries.MOB_EFFECT,
                        ResourceLocation.fromNamespaceAndPath("ranger", "backstep_effect")
                )
        ))) {
            return false;
        }
        return original;
    }
}
