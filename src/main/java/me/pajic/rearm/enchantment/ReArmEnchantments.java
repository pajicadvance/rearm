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

    public static void updateProtectionEnchantments(ItemEnchantments.Mutable existing, ItemEnchantments addition) {
        for (Holder<Enchantment> e : addition.keySet()) {
            if (e.is(EnchantmentTags.ARMOR_EXCLUSIVE)) {
                if (existing.getLevel(e) == 0) {
                    existing.set(e, addition.getLevel(e));
                }
                else {
                    existing.upgrade(e, addition.getLevel(e));
                }
            }
        }
    }
}
