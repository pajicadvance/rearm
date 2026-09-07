package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.ReArm;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    //~ if <26.1 'enchantment()' -> 'enchantment' {
    @WrapMethod(method = "filterCompatibleEnchantments")
    private static void allowMultipleProtectionEnchantments(List<EnchantmentInstance> enchants, EnchantmentInstance target, Operation<Void> original) {
        if (ReArm.CONFIG.protection.allowMultipleProtectionEnchantments.get() && target.enchantment().is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
            int itemProtEnchants = 0;
            for (EnchantmentInstance ei : enchants) {
                if (!ei.enchantment().equals(target.enchantment()) && ei.enchantment().is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
                    itemProtEnchants++;
                }
            }
            if (itemProtEnchants >= ReArm.CONFIG.protection.maxProtectionEnchantments.get()) {
                original.call(enchants, target);
            }
        }
        else {
            original.call(enchants, target);
        }
    }
    //~}
}
