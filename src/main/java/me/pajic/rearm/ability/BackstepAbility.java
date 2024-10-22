package me.pajic.rearm.ability;

import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class BackstepAbility {

    public static boolean tryBackstep(KeyMapping backstepKey, Minecraft client) {
        if (
                backstepKey.isDown() && client.level != null && client.player != null &&
                client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)
        ) {
            Player player = client.player;
            int backstepLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BACKSTEP),
                    player.getMainHandItem()
            );
            Vec3 look = player.getViewVector(1);
            player.setDeltaMovement(
                    -look.x / (4 - backstepLevel),
                    player.getAttributeValue(Attributes.JUMP_STRENGTH),
                    -look.z / (4 - backstepLevel)
            );
            if (player.isSprinting()) {
                player.causeFoodExhaustion(0.2F);
            } else {
                player.causeFoodExhaustion(0.05F);
            }
            backstepKey.setDown(false);
            return true;
        }
        return false;
    }
}
