package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ProjectileWeaponItem;
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
    public boolean weaponCondition(Minecraft client) {
        return client.player.getWeaponItem().getItem() instanceof ProjectileWeaponItem;
    }

    @Override
    public boolean enchantmentCondition(Minecraft client) {
        return EnchantmentHelper.getItemEnchantmentLevel(
                client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(Enchantments.PIERCING),
                client.player.getWeaponItem()
        ) > 0;
    }
}
