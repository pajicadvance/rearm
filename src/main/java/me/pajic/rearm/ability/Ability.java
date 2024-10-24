package me.pajic.rearm.ability;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface Ability {

    default boolean tryAbility(KeyMapping abilityKey, Minecraft client) {
        if (
                abilityKey.isDown() && client.level != null && client.player != null &&
                configCondition() && weaponCondition(client.player.getWeaponItem()) &&
                enchantmentCondition(
                        client.player.getWeaponItem(),
                        client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                )
        ) {
            CooldownTracker.activeItemClient = client.player.getWeaponItem().copy();
            ClientPlayNetworking.send(new AbilityManager.C2STriggerAbilityPayload(
                    CooldownTracker.activeItemClient, abilityType(), client.player.getUUID())
            );
            CooldownTracker.abilityType = abilityType();
            abilityKey.setDown(false);
            return true;
        }
        return false;
    }

    default boolean shouldRenderHotbarActiveIndicator(ItemStack stack, LocalPlayer localPlayer) {
        return localPlayer != null && CooldownTracker.abilityType == abilityType() &&
                configCondition() && ItemStack.matches(stack, CooldownTracker.activeItemClient);
    }

    default boolean shouldRenderHotbarCooldownIndicator(ItemStack stack, Minecraft client) {
        return client.player != null && CooldownTracker.abilityUsed && configCondition() && weaponCondition(stack) &&
                enchantmentCondition(stack, client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT));
    }

    default boolean shouldTriggerAbility(ItemStack weapon, Entity player) {
        return AbilityManager.getPlayerAbilityType(player.getUUID()) == abilityType() &&
                ItemStack.matches(AbilityManager.getPlayerActiveItem(player.getUUID()), weapon);
    }

    AbilityType abilityType();

    boolean configCondition();

    boolean weaponCondition(ItemStack itemStack);

    boolean enchantmentCondition(ItemStack itemStack, Registry<Enchantment> registry);
}
