package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.AbilityManager;
import me.pajic.rearm.ability.CooldownTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Shadow @Final private Minecraft minecraft;

    @Expression("? > 0.0")
    @ModifyExpressionValue(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean renderAbilityCooldown(boolean original,
                                          @Local(argsOnly = true) ItemStack stack,
                                          @Local LocalFloatRef f,
                                          @Local LocalPlayer localPlayer
    ) {
        if (AbilityManager.shouldRenderHotbarActiveIndicator(stack, localPlayer)) {
            f.set(1);
            return minecraft.level != null && minecraft.level.getGameTime() % 10 > 0 && minecraft.level.getGameTime() % 10 < 5;
        }
        if (AbilityManager.shouldRenderHotbarCooldownIndicator(stack, minecraft)) {
            f.set(Mth.clamp((float) CooldownTracker.abilityCooldown / Main.CONFIG.abilityCooldown(), 0.0F, 1.0F));
            return true;
        }
        return original;
    }
}
