package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CombatRules.class)
public class CombatRulesMixin {

    @ModifyArgs(
            method = "getDamageAfterAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(FFF)F",
                    ordinal = 0
            )
    )
    private static void modifyReduction(
            Args args,
            @Local(argsOnly = true, ordinal = 0) float damage,
            @Local(argsOnly = true, ordinal = 1) float armor,
            @Local(argsOnly = true, ordinal = 2) float toughness
    ) {
        if (Main.CONFIG.armor.armorRebalance.get()) {
            float m = Main.CONFIG.armor.armorMultiplier.get();
            float t = (3.5F * toughness) / (toughness + 1);
            float d = (6 * damage) / (toughness + 8);
            float l = Mth.lerp(t / 20, 1 / 5F, 1 / 3.8F);

            args.set(0, ((armor / 2 + t) - d) * m);
            args.set(1, armor * l * m);
            args.set(2, 20 * m);
        }
    }

    @ModifyExpressionValue(
            method = "getDamageAfterAbsorb",
            at = @At(
                    value = "CONSTANT",
                    args = "floatValue=25.0"
            )
    )
    private static float modifyProtectionDivisor(float original) {
        return Main.CONFIG.armor.armorRebalance.get() ? original * Main.CONFIG.armor.armorMultiplier.get() : original;
    }

    @ModifyReturnValue(
            method = "getDamageAfterAbsorb",
            at = @At("RETURN")
    )
    private static float debugAfterAbsorb(float original, @Local(argsOnly = true, ordinal = 0) float damage, @Local(name = "f3") float f3) {
        Main.debugLog("Damage: {}", damage);
        Main.debugLog("Damage after absorb: {}", original);
        Main.debugLog("Damage reduction: {}%", f3 * 100);
        return original;
    }

    @ModifyReturnValue(
            method = "getDamageAfterMagicAbsorb",
            at = @At("RETURN")
    )
    private static float debugAfterMagicAbsorb(float original) {
        Main.debugLog("Damage after magic absorb: {}", original);
        return original;
    }
}
