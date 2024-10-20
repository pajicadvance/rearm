package me.pajic.rearm.ability;

import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

public class BackstepAbility {

    public static boolean tryBackstep(KeyMapping backstepKey, Minecraft client) {
        if (
                // Check if backstep key got pressed while under the Backstep status effect
                backstepKey.isDown() && client.level != null && client.player != null &&
                client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)
        ) {
            Player player = client.player;
            // Get backstep enchantment level
            int backstepLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(
                            ResourceKey.create(
                                    Registries.ENCHANTMENT,
                                    ResourceLocation.fromNamespaceAndPath("rearm", "backstep")
                            )
                    ),
                    player.getMainHandItem()
            );
            // Launch player
            Vec3 look = player.getViewVector(1);
            player.setDeltaMovement(
                    -look.x / (4 - backstepLevel),
                    player.getAttributeValue(Attributes.JUMP_STRENGTH),
                    -look.z / (4 - backstepLevel)
            );
            // Apply exhaustion equal to jump exhaustion
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
