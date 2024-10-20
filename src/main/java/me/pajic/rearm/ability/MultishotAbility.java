package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class MultishotAbility {

    public static ItemStack ACTIVE_ITEM_CLIENT;
    public static boolean MULTISHOT_READY_CLIENT;

    public static boolean tryMultishot(KeyMapping abilityKey, Minecraft client) {
        if (
                // Check if ability key got pressed while holding a ranged weapon with Multishot
                Main.CONFIG.abilities.multishotAbility() &&
                abilityKey.isDown() && client.level != null && client.player != null &&
                client.player.getWeaponItem().getItem() instanceof ProjectileWeaponItem &&
                EnchantmentHelper.getItemEnchantmentLevel(
                        client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                                .getHolderOrThrow(Enchantments.MULTISHOT),
                        client.player.getWeaponItem()
                ) > 0
        ) {
            ACTIVE_ITEM_CLIENT = client.player.getWeaponItem().copy();
            ClientPlayNetworking.send(new AbilityNetworking.C2STriggerMultishotAbilityPayload(ACTIVE_ITEM_CLIENT, client.player.getUUID()));
            MULTISHOT_READY_CLIENT = true;
            abilityKey.setDown(false);
            return true;
        }
        return false;
    }

    public static boolean shouldRenderMultishotReadyHotbarIndicator(ItemStack stack, LocalPlayer localPlayer) {
        return localPlayer != null && MULTISHOT_READY_CLIENT && Main.CONFIG.abilities.multishotAbility() && ItemStack.matches(stack, ACTIVE_ITEM_CLIENT);
    }
}
