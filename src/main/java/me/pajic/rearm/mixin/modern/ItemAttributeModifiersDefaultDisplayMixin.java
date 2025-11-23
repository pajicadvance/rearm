package me.pajic.rearm.mixin.modern;

//? if > 1.21.1 {

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemAttributeModifiers.Display.Default.class)
public class ItemAttributeModifiersDefaultDisplayMixin {

    @SuppressWarnings({"deprecation"})
    @ModifyExpressionValue(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/text/DecimalFormat;format(D)Ljava/lang/String;",
                    ordinal = 1
            )
    )
    private String changeKnockbackResistDisplayStyle(String original, @Local(argsOnly = true) Holder<Attribute> attribute, @Local(ordinal = 0) double d) {
        return attribute.is(Attributes.KNOCKBACK_RESISTANCE) ?
                ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(d * 100) + "%" :
                original;
    }
}
//?}
