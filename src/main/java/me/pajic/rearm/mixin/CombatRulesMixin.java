package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@IfModAbsent("apothic_attributes")
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
            @Local(argsOnly = true, name = "damage") float damage,
            @Local(argsOnly = true, name = "totalArmor") float totalArmor,
            @Local(argsOnly = true, name = "armorToughness") float armorToughness
    ) {
        if (CompatFlags.armorRebalanceActive()) {
            float m = ReArm.CONFIG.armor.armorMultiplier.get();
            float t = (3.5F * armorToughness) / (armorToughness + 1);
            float d = (6 * damage) / (armorToughness + 8);
            float l = Mth.lerp(t / 20, 1 / 5F, 1 / 3.8F);

            args.set(0, ((totalArmor / 2 + t) - d) * m);
            args.set(1, totalArmor * l * m);
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
        return CompatFlags.armorRebalanceActive() ? original * ReArm.CONFIG.armor.armorMultiplier.get() : original;
    }
}
