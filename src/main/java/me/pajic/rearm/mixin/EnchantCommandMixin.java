package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.server.commands.EnchantCommand;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantCommand.class)
public class EnchantCommandMixin {

    @ModifyExpressionValue(
            method = "enchant",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;isEnchantmentCompatible(Ljava/util/Collection;Lnet/minecraft/core/Holder;)Z"
            )
    )
    private static boolean allowMultipleProtectionEnchantments(
            boolean original,
            @Local(name = "item") ItemStack item,
            @Local(argsOnly = true, name = "enchantmentHolder") Holder<Enchantment> enchantmentHolder,
            @Local(argsOnly = true, name = "level") int level
    ) {
        if (ReArm.CONFIG.protection.allowMultipleProtectionEnchantments.get() && enchantmentHolder.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
            ItemEnchantments.Mutable protectionEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            ReArmEnchantments.updateProtectionEnchantments(protectionEnchantments, EnchantmentHelper.getEnchantmentsForCrafting(item));
            if (protectionEnchantments.getLevel(enchantmentHolder) == 0) {
                protectionEnchantments.set(enchantmentHolder, level);
            }
            else {
                protectionEnchantments.upgrade(enchantmentHolder, level);
            }
            if (protectionEnchantments.keySet().size() <= ReArm.CONFIG.protection.maxProtectionEnchantments.get()) {
                return true;
            }
        }
        return original;
    }
}
