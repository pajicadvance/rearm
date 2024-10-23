package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class SweepingEdgeAbility implements Ability {
    @Override
    public AbilityType abilityType() {
        return AbilityType.SWEEPING_EDGE;
    }

    @Override
    public boolean configCondition() {
        return Main.CONFIG.sweepingEdge.sweepingEdgeAbility();
    }

    @Override
    public boolean weaponCondition(ItemStack itemStack) {
        return itemStack.is(ItemTags.SWORDS);
    }

    @Override
    public boolean enchantmentCondition(ItemStack itemStack, Registry<Enchantment> registry) {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(Enchantments.SWEEPING_EDGE), itemStack) > 0;
    }
}
