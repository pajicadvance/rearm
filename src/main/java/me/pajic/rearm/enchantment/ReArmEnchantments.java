package me.pajic.rearm.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ReArmEnchantments {

    public static final ResourceKey<Enchantment> BACKSTEP = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("rearm", "backstep")
    );

    public static final ResourceKey<Enchantment> CRIPPLING_BLOW = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath("rearm", "crippling_blow")
    );

    public static void updateProtectionEnchantments(ItemEnchantments.Mutable existing, ItemEnchantments addition) {
        for (Holder<Enchantment> enchantment : addition.keySet()) {
            if (enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
                if (existing.getLevel(enchantment) == 0) {
                    existing.set(enchantment, addition.getLevel(enchantment));
                }
                else {
                    existing.upgrade(enchantment, addition.getLevel(enchantment));
                }
            }
        }
    }
}
