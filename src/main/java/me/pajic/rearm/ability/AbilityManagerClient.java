package me.pajic.rearm.ability;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class AbilityManagerClient {

    public static boolean tryAbilities(KeyMapping abilityKey, Minecraft client) {
        for (Ability ability : AbilityManager.abilities) {
            if (ability.tryAbility(abilityKey, client)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldRenderHotbarActiveIndicator(ItemStack stack, LocalPlayer player) {
        for (Ability ability : AbilityManager.abilities) {
            if (ability.shouldRenderHotbarActiveIndicator(stack, player)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldRenderHotbarCooldownIndicator(ItemStack stack, Minecraft client) {
        for (Ability ability : AbilityManager.abilities) {
            if (ability.shouldRenderHotbarCooldownIndicator(stack, client)) {
                return true;
            }
        }
        return false;
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(AbilityManager.S2CResetAbilityTypePayload.TYPE, (payload, context) ->
                CooldownTracker.abilityType = AbilityType.NONE
        );
        ClientPlayNetworking.registerGlobalReceiver(AbilityManager.S2CSignalAbilityUsedPayload.TYPE, (payload, context) ->
                CooldownTracker.abilityUsed = true
        );
    }
}
