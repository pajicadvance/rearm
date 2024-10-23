package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CripplingBlowAbility implements Ability {
    @Override
    public AbilityType abilityType() {
        return AbilityType.CRIPPLING_BLOW;
    }

    @Override
    public boolean configCondition() {
        return Main.CONFIG.cripplingBlow.cripplingBlowAbility();
    }

    @Override
    public boolean weaponCondition(ItemStack itemStack) {
        return itemStack.is(ItemTags.AXES);
    }

    @Override
    public boolean enchantmentCondition(ItemStack itemStack, Registry<Enchantment> registry) {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(ReArmEnchantments.CRIPPLING_BLOW), itemStack) > 0;
    }
}
