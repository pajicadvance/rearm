package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
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
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/Enchantment;areCompatible(Lnet/minecraft/core/Holder;Lnet/minecraft/core/Holder;)Z"
            )
    )
    private boolean allowMultipleProtectionEnchantments(
            boolean original,
            @Local ItemEnchantments ie1,
            @Local ItemEnchantments.Mutable ie2,
            @Local(ordinal = 0) Holder<Enchantment> holder1,
            @Local(ordinal = 1) Holder<Enchantment> holder2
    ) {
        if (
                Main.CONFIG.allowMultipleProtectionEnchantments() &&
                holder1.is(EnchantmentTags.ARMOR_EXCLUSIVE) &&
                holder2.is(EnchantmentTags.ARMOR_EXCLUSIVE)
        ) {
            ItemEnchantments.Mutable protectionEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            ReArmEnchantments.updateProtectionEnchantments(protectionEnchantments, ie1);
            ReArmEnchantments.updateProtectionEnchantments(protectionEnchantments, ie2.toImmutable());
            if (protectionEnchantments.keySet().size() <= Main.CONFIG.maxProtectionEnchantments()) {
                return true;
            }
        }
        return original;
    }
}
