package me.pajic.ranger.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.ranger.Main;
import me.pajic.ranger.ability.BackstepAbility;
import me.pajic.ranger.ability.CooldownTracker;
import me.pajic.ranger.ability.MultishotAbility;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class RangerKeybinds {

    private static final KeyMapping ABILITY_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.ranger.ability",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category.ranger.keybindings"
            )
    );

    private static final KeyMapping BACKSTEP_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.ranger.backstep",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    "category.ranger.keybindings"
            )
    );

    public static void initKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (CooldownTracker.BACKSTEP_CD == 0) {
                if (BackstepAbility.tryBackstep(BACKSTEP_KEY, client)) {
                    CooldownTracker.BACKSTEP_CD = Main.CONFIG.bow.backstepTimeframe();
                }
            }
            if (CooldownTracker.ABILITY_CD == 0) {
                if (MultishotAbility.tryMultishot(ABILITY_KEY, client)) {
                    CooldownTracker.ABILITY_CD = Main.CONFIG.abilities.abilityCooldown();
                }
            }
        });
    }
}
