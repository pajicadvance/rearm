package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ReArmKeybinds {
    private static final Lazy<KeyMapping> ACTION_KEY = Lazy.of(() ->
            new KeyMapping(
                    "key.rearm.action",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    "category.rearm.keybindings"
            )
    );

    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ACTION_KEY.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (ACTION_KEY.get().isDown() && client.level != null && client.player != null) {
            if (CooldownTracker.backstepCooldown == 0) {
                if (tryBackstep(ACTION_KEY.get(), client)) {
                    CooldownTracker.backstepCooldown = Main.CONFIG.bow.backstepTimeframe.get();
                }
            }
            ReArmNetworking.sendToServer(new ReArmNetworking.C2SUpdatePlayerRecallCondition(client.player.getUUID()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean tryBackstep(KeyMapping actionKey, Minecraft client) {
        if (client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)) {
            Player player = client.player;
            int backstepLevel = Math.min(EnchantmentHelper.getTagEnchantmentLevel(
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
            ReArmNetworking.sendToServer(new ReArmNetworking.C2SCauseBackstepExhaustionPayload(5.0F));
            actionKey.setDown(false);
            return true;
        }
        return false;
    }
}
