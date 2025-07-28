package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.Main;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @WrapMethod(method = "filterCompatibleEnchantments")
    private static void allowMultipleProtectionEnchantments(List<EnchantmentInstance> dataList, EnchantmentInstance data, Operation<Void> original) {
        if (Main.CONFIG.protection.allowMultipleProtectionEnchantments.get() && data.enchantment/*? if >= 1.21.7 {*//*()*//*?}*/.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
            int itemProtEnchants = 0;
            for (EnchantmentInstance ei : dataList) {
                if (!ei.enchantment/*? if >= 1.21.7 {*//*()*//*?}*/.equals(data.enchantment/*? if >= 1.21.7 {*//*()*//*?}*/) && ei.enchantment/*? if >= 1.21.7 {*//*()*//*?}*/.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
                    itemProtEnchants++;
                }
            }
            if (itemProtEnchants >= Main.CONFIG.protection.maxProtectionEnchantments.get()) {
                original.call(dataList, data);
            }
        }
        else {
            original.call(dataList, data);
        }
    }
}
