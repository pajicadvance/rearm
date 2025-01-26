package me.pajic.rearm.compat;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.recipe.special.EmiAnvilEnchantRecipe;
import me.pajic.rearm.Main;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class EmiCompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        for (Item item : EmiPort.getItemRegistry()) {
            if (Main.CONFIG.crossbow.acceptPower() && item instanceof CrossbowItem)
                addAnvilEnchantingRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.POWER));
            if (Main.CONFIG.crossbow.acceptInfinity() && item instanceof CrossbowItem)
                addAnvilEnchantingRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.INFINITY));
            if (Main.CONFIG.axe.acceptKnockback() && item instanceof AxeItem)
                addAnvilEnchantingRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.KNOCKBACK));
            if (Main.CONFIG.axe.acceptLooting() && item instanceof AxeItem)
                addAnvilEnchantingRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.LOOTING));
            if (Main.CONFIG.bow.acceptMultishot() && item instanceof BowItem)
                addAnvilEnchantingRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.MULTISHOT));
            if (Main.CONFIG.sword.rejectKnockback() && item instanceof SwordItem)
                removeAnvilEnchantRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.KNOCKBACK));
            if (Main.CONFIG.crossbow.rejectMultishot() && item instanceof CrossbowItem)
                removeAnvilEnchantRecipe(registry, item, EmiPort.getEnchantmentRegistry().get(Enchantments.MULTISHOT));
        }
    }

    private void removeAnvilEnchantRecipe(EmiRegistry registry, Item item, Enchantment enchantment) {
        registry.removeRecipes(EmiPort.id("emi", "/anvil/enchanting/" + EmiUtil.subId(item) + "/" + EmiUtil.subId(EmiPort.getEnchantmentRegistry().getKey(enchantment)) + "/" + enchantment.getMaxLevel()));
    }

    private void addAnvilEnchantingRecipe(EmiRegistry registry, Item item, Enchantment enchantment) {
        registry.addRecipe(
                new EmiAnvilEnchantRecipe(
                        item,
                        enchantment,
                        enchantment.getMaxLevel(),
                        EmiPort.id("emi", "/anvil/enchanting/" + EmiUtil.subId(item) + "/" + EmiUtil.subId(EmiPort.getEnchantmentRegistry().getKey(enchantment)) + "/" + enchantment.getMaxLevel())
                )
        );
    }
}
