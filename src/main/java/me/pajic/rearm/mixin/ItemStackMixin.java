package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? <26.1 {
/*import net.minecraft.world.item.ArmorItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
*///?} else {
import net.minecraft.world.item.equipment.Equippable;
import org.apache.commons.lang3.function.TriConsumer;
import net.minecraft.core.component.DataComponents;
//?}

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract ItemEnchantments getEnchantments();

    //? <26.1 {
    /*@SuppressWarnings({"deprecation"})
    @ModifyExpressionValue(
            method = "addModifierTooltip",
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
    *///?}

    @ModifyReceiver(
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"
            )
    )
    private ItemAttributeModifiers addToughness(ItemAttributeModifiers instance, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        return rearm$addToughness(instance);
    }

    @SuppressWarnings("rawtypes")
    @ModifyReceiver(
            //~ if <26.1 'Lorg/apache/commons/lang3/function/TriConsumer;' -> 'Ljava/util/function/BiConsumer;' {
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"
            )
            //~}
    )
    //~ if <26.1 'TriConsumer' -> 'BiConsumer'
    private ItemAttributeModifiers addToughness(ItemAttributeModifiers instance, EquipmentSlotGroup slot, TriConsumer consumer) {
        return rearm$addToughness(instance);
    }

    @Unique
    private ItemAttributeModifiers rearm$addToughness(ItemAttributeModifiers instance) {
        if (CompatFlags.armorRebalanceActive() && ReArm.CONFIG.armor.enchantmentBasedToughness.get()) {
            ItemStack self = (ItemStack) (Object) this;
            //~ if <26.1 'self.has(DataComponents.EQUIPPABLE)' -> 'self.getItem() instanceof ArmorItem armor'
            if (self.has(DataComponents.EQUIPPABLE)) {
                //? >=26.1
                Equippable equippable = self.get(DataComponents.EQUIPPABLE);
                double toughness = 0;
                for (Holder<Enchantment> holder : getEnchantments().keySet()) {
                    String enchantment = holder.getRegisteredName().split(":")[1];
                    if (ReArm.CONFIG.armor.toughnessPerEnchantment.get().containsKey(enchantment)) {
                        int level = EnchantmentHelper.getItemEnchantmentLevel(holder, self);
                        toughness += ReArm.CONFIG.armor.toughnessPerEnchantment.get().get(enchantment) * level;
                    }
                }
                @SuppressWarnings("DataFlowIssue")
                String type =
                        //? >=26.1 {
                        switch (equippable.slot()) {
                            case HEAD -> "helmet";
                            case CHEST -> "chestplate";
                            case LEGS -> "leggings";
                            case FEET -> "boots";
                            default -> "body";
                        };
                        //?} else {
                        /*armor.getType().getName();
                        *///?}
                return instance.withModifierAdded(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        Identifier.withDefaultNamespace("armor." + type),
                        toughness,
                        AttributeModifier.Operation.ADD_VALUE
                //~ if <26.1 'equippable.slot()' -> 'armor.getType().getSlot()'
                ), EquipmentSlotGroup.bySlot(equippable.slot()));
            }
        }
        return instance;
    }
}
