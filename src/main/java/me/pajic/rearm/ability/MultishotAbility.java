package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class MultishotAbility implements Ability {

    @Override
    public AbilityType abilityType() {
        return AbilityType.MULTISHOT;
    }

    @Override
    public boolean configCondition() {
        return Main.CONFIG.multishot.multishotAbility();
    }

    @Override
    public boolean weaponCondition(ItemStack itemStack) {
        return ReArmItems.isRangedWeapon(itemStack);
    }

    @Override
    public boolean enchantmentCondition(ItemStack itemStack, Registry<Enchantment> registry) {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(Enchantments.MULTISHOT), itemStack) > 0;
    }
}
