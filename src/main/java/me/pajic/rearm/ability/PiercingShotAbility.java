package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
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
        return Main.CONFIG.piercingShot.piercingShotAbility();
    }

    @Override
    public boolean weaponCondition(ItemStack itemStack) {
        return ReArmItems.isCrossbow(itemStack) &&
                itemStack.getComponents().get(DataComponents.CHARGED_PROJECTILES).equals(ChargedProjectiles.EMPTY);
    }

    @Override
    public boolean enchantmentCondition(ItemStack itemStack, Registry<Enchantment> registry) {
        return EnchantmentHelper.getItemEnchantmentLevel(registry.getHolderOrThrow(Enchantments.PIERCING), itemStack) > 0;
    }
}
