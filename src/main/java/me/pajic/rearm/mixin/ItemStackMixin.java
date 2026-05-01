package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import me.pajic.rearm.ReArm;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.equipment.Equippable;
import org.apache.commons.lang3.function.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract ItemEnchantments getEnchantments();

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
            method = "forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/ItemAttributeModifiers;forEach(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"
            )
    )
    private ItemAttributeModifiers addToughness(ItemAttributeModifiers instance, EquipmentSlotGroup slot, TriConsumer consumer) {
        return rearm$addToughness(instance);
    }

    @Unique
    private ItemAttributeModifiers rearm$addToughness(ItemAttributeModifiers instance) {
        if (ReArm.armorRebalanceActive() && ReArm.CONFIG.armor.enchantmentBasedToughness.get()) {
            ItemStack self = (ItemStack) (Object) this;
            if (self.has(DataComponents.EQUIPPABLE)) {
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
                String type = switch (equippable.slot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "body";
                };
                return instance.withModifierAdded(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        Identifier.withDefaultNamespace("armor." + type),
                        toughness,
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.bySlot(equippable.slot()));
            }
        }
        return instance;
    }
}
