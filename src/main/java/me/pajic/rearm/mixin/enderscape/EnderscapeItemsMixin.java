package me.pajic.rearm.mixin.enderscape;

//? >=26.1 {

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.ArmorMaterialHelper;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@IfModLoaded("enderscape")
@Mixin(EnderscapeItems.class)
public class EnderscapeItemsMixin {

    @Shadow @Final private static ArmorMaterial SHADOLINE_ARMOR_MATERIAL;

    @ModifyReceiver(
            method = "shadolineArmorProperties",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;build()Lnet/minecraft/world/item/component/ItemAttributeModifiers;"
            )
    )
    private static ItemAttributeModifiers.Builder addShadolineKnockbackResist(
            ItemAttributeModifiers.Builder instance,
            @Local Identifier location,
            @Local EquipmentSlotGroup group
    ) {
        return CompatFlags.armorRebalanceActive() ? instance.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                location, SHADOLINE_ARMOR_MATERIAL.knockbackResistance(), AttributeModifier.Operation.ADD_VALUE
        ), group) : instance;
    }

    @Definition(id = "builder", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;builder()Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "add", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "AttributeModifier", type = AttributeModifier.class)
    @Definition(id = "id", method = "Lnet/penumbra/enderscape/Enderscape;id(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;")
    @Expression("builder().add(?,new AttributeModifier(id('drift_leggings_armor'),@(4.0),?),?)")
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static double modifyDriftshadeLeggingsDefense(double original) {
        return CompatFlags.armorRebalanceActive() ? ArmorMaterialHelper.calculateDefenses(rearm$shadolineTotal()).get(ArmorType.LEGGINGS) : original;
    }

    @Definition(id = "builder", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;builder()Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "add", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "AttributeModifier", type = AttributeModifier.class)
    @Definition(id = "id", method = "Lnet/penumbra/enderscape/Enderscape;id(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;")
    @Definition(id = "ARMOR_TOUGHNESS", field = "Lnet/minecraft/world/entity/ai/attributes/Attributes;ARMOR_TOUGHNESS:Lnet/minecraft/core/Holder;")
    @Expression("builder().add(?,new AttributeModifier(id('drift_leggings_armor'),?,?),?).add(ARMOR_TOUGHNESS,?,?)")
    @WrapOperation(
            method = "<clinit>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static ItemAttributeModifiers.Builder removeDriftshadeLeggingsToughness(ItemAttributeModifiers.Builder instance, Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot, Operation<ItemAttributeModifiers.Builder> original) {
        return CompatFlags.armorRebalanceActive() ? instance : original.call(instance, attribute, modifier, slot);
    }

    @Definition(id = "builder", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;builder()Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "add", method = "Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;add(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lnet/minecraft/world/item/component/ItemAttributeModifiers$Builder;")
    @Definition(id = "AttributeModifier", type = AttributeModifier.class)
    @Definition(id = "id", method = "Lnet/penumbra/enderscape/Enderscape;id(Ljava/lang/String;)Lnet/minecraft/resources/Identifier;")
    @Expression("builder().add(?,new AttributeModifier(id('drift_leggings_armor'),?,?),?)")
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static ItemAttributeModifiers.Builder addDriftshadeLeggingsKnockbackResist(ItemAttributeModifiers.Builder original) {
        return CompatFlags.armorRebalanceActive() ? original.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                Identifier.parse("enderscape:drift_leggings_knockback_resist"),
                ArmorMaterialHelper.calculateKnockbackResist(rearm$shadolineTotal()),
                AttributeModifier.Operation.ADD_VALUE
        ), EquipmentSlotGroup.LEGS) : original;
    }

    @Unique
    private static int rearm$shadolineTotal() {
        return ReArm.CONFIG.armor.totalArmorOverrides.getOrDefault(Identifier.parse("enderscape:shadoline"), 24);
    }
}
//?}
