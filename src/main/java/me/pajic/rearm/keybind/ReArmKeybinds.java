package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.*;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class ReArmKeybinds {

    //? if > 1.21.1 {
    private static final KeyMapping.Category MOD_KEYS = KeyMapping.Category.register(
            ReArm.id("keys")
    );
    //?}

    public static final KeyMapping ACTION_KEY = new KeyMapping(
			"key.rearm.action",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT_ALT,
			/*? if 1.21.1 {*//*"category.rearm.keybindings"*//*?} else {*/MOD_KEYS/*?}*/
	);

	public static void onClientTick(Minecraft client) {
		if (ACTION_KEY.isDown() && client.level != null && client.player != null) {
			if (CooldownTracker.backstepCooldown == 0) {
				if (tryBackstep(ACTION_KEY, client)) {
					CooldownTracker.backstepCooldown = ReArm.CONFIG.bow.backstepTimeframe.get();
				}
			}
			ReArm.xplat().sendToServer(new CripplingThrowAbility.C2SUpdatePlayerRecallCondition(client.player.getUUID()));
			if (ReArm.CONFIG.shield.enableBash.get()) ReArm.xplat().sendToServer(new BashAbility.C2SBashSignal());
		}
	}

    @SuppressWarnings("DataFlowIssue")
    public static boolean tryBackstep(KeyMapping actionKey, Minecraft client) {
        if (client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)) {
            Player player = client.player;
            int backstepLevel = Math.min(EnchantmentHelper.getItemEnchantmentLevel(
                    //? if 1.21.1
                    /*client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BACKSTEP),*/
                    //? if > 1.21.1
                    client.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.BACKSTEP),
                    player.getMainHandItem()
            ), 3);
            Vec3 look = player.getViewVector(1);
            player.setDeltaMovement(
                    -look.x / (4 - backstepLevel),
                    player.getAttributeValue(Attributes.JUMP_STRENGTH),
                    -look.z / (4 - backstepLevel)
            );
			ReArm.xplat().sendToServer(new BackstepAbility.C2SCauseBackstepExhaustionPayload(5.0F));
            actionKey.setDown(false);
            return true;
        }
        return false;
    }
}
