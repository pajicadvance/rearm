package me.pajic.rearm.mixin.enchantment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.ReArm;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    //~ if <26.1 'enchantment()' -> 'enchantment' {
    @WrapOperation(
            method = "selectEnchantment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;filterCompatibleEnchantments(Ljava/util/List;Lnet/minecraft/world/item/enchantment/EnchantmentInstance;)V"
            )
    )
    private static void allowMultipleProtectionEnchantments(
            List<EnchantmentInstance> enchants,
            EnchantmentInstance target,
            Operation<Void> original,
            @Local(ordinal = 0) List<EnchantmentInstance> selected
    ) {
        if (!ReArm.CONFIG.protection.allowMultipleProtectionEnchantments.get()) {
            original.call(enchants, target);
            return;
        }
        long selectedProtEnchants = selected.stream().filter(ei -> ei.enchantment().is(EnchantmentTags.ARMOR_EXCLUSIVE)).count();
        boolean limitReached = selectedProtEnchants >= ReArm.CONFIG.protection.maxProtectionEnchantments.get();
        boolean targetIsProt = target.enchantment().is(EnchantmentTags.ARMOR_EXCLUSIVE);
        enchants.removeIf(ei -> {
            boolean isProt = ei.enchantment().is(EnchantmentTags.ARMOR_EXCLUSIVE);
            if (isProt && limitReached) return true;
            if (isProt && targetIsProt && !ei.enchantment().equals(target.enchantment())) return false;
            return !Enchantment.areCompatible(target.enchantment(), ei.enchantment());
        });
    }
    //~}
}
