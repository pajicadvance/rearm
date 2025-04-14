package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
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
                    if (BackstepAbility.tryBackstep(ACTION_KEY, client)) {
                        CooldownTracker.backstepCooldown = Main.CONFIG.bow.backstepTimeframe();
                    }
                }
                ClientPlayNetworking.send(new CripplingThrowAbility.C2SUpdatePlayerRecallCondition(client.player.getUUID()));
            }
        });
    }
}
