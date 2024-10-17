package me.pajic.ranger.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.ranger.effect.RangerEffects;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class RangerKeybinds {

    private static final KeyMapping BACKSTEP = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.ranger.backstep",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    "category.ranger.keybindings"
            )
    );

    public static void initKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (
                    // Check if backstep keybind got pressed while under the backstep status effect
                    BACKSTEP.consumeClick() && client.level != null && client.player != null &&
                    client.player.hasEffect(RangerEffects.BACKSTEP_EFFECT)
            ) {
                Player player = client.player;
                // Get backstep enchantment level
                int backstepLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(
                                ResourceKey.create(
                                        Registries.ENCHANTMENT,
                                        ResourceLocation.fromNamespaceAndPath("ranger", "backstep")
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
            }
        });
    }
}
