package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class PiercingShotAbility implements Ability {

    @Override
    public AbilityType abilityType() {
        return AbilityType.PIERCING_SHOT;
    }

    @Override
    public boolean configCondition() {
        return Main.CONFIG.abilities.piercingShotAbility();
    }

    @Override
    public boolean weaponCondition(ItemStack stack) {
        return ReArmItems.isRangedWeapon(stack);
    }

    @Override
    public boolean enchantmentCondition(ItemStack stack, Registry<Enchantment> registry) {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(Enchantments.PIERCING), stack) > 0;
    }
}
