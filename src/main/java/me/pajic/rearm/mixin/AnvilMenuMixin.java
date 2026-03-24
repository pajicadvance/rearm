package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @ModifyExpressionValue(
			//? if fabric
            method = "createResult",
			//? if neoforge
			//method = "createResultInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"
            )
    )
    private boolean allowMultipleProtectionEnchantments(
            boolean original,
            @Local(name = "additionalEnchantments") ItemEnchantments additionalEnchantments,
            @Local(name = "enchantments") ItemEnchantments.Mutable enchantments,
            @Local(name = "enchantmentHolder") Holder<Enchantment> enchantmentHolder,
            @Local(name = "other") Holder<Enchantment> other
    ) {
        if (
                ReArm.CONFIG.protection.allowMultipleProtectionEnchantments.get() &&
                enchantmentHolder.is(EnchantmentTags.ARMOR_EXCLUSIVE) &&
                other.is(EnchantmentTags.ARMOR_EXCLUSIVE)
        ) {
            ItemEnchantments.Mutable protectionEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            ReArmEnchantments.updateProtectionEnchantments(protectionEnchantments, additionalEnchantments);
            ReArmEnchantments.updateProtectionEnchantments(protectionEnchantments, enchantments.toImmutable());
            if (protectionEnchantments.keySet().size() <= ReArm.CONFIG.protection.maxProtectionEnchantments.get()) {
                return true;
            }
        }
        return original;
    }
}
