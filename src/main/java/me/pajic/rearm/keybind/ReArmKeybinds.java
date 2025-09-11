package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.*;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class ReArmKeybinds {

    private static final KeyMapping ACTION_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.rearm.action",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    "category.rearm.keybindings"
            )
    );

    public static void initKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ACTION_KEY.isDown() && client.level != null && client.player != null) {
                if (CooldownTracker.backstepCooldown == 0) {
                    if (tryBackstep(ACTION_KEY, client)) {
                        CooldownTracker.backstepCooldown = Main.CONFIG.bow.backstepTimeframe.get();
                    }
                }
                ClientPlayNetworking.send(new CripplingThrowAbility.C2SUpdatePlayerRecallCondition(client.player.getUUID()));
                if (Main.CONFIG.shield.enableBash.get()) ClientPlayNetworking.send(new BashAbility.C2SBashSignal());
            }
        });
    }

    public static boolean tryBackstep(KeyMapping actionKey, Minecraft client) {
        if (client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)) {
            Player player = client.player;
            int backstepLevel = Math.min(EnchantmentHelper.getItemEnchantmentLevel(
                    //? if 1.21.1
                    client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BACKSTEP),
                    //? if >= 1.21.7
                    /*client.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.BACKSTEP),*/
                    player.getMainHandItem()
            ), 3);
            Vec3 look = player.getViewVector(1);
            player.setDeltaMovement(
                    -look.x / (4 - backstepLevel),
                    player.getAttributeValue(Attributes.JUMP_STRENGTH),
                    -look.z / (4 - backstepLevel)
            );
            ClientPlayNetworking.send(new BackstepAbility.C2SCauseBackstepExhaustionPayload(5.0F));
            actionKey.setDown(false);
            return true;
        }
        return false;
    }
}
