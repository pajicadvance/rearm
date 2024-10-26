package me.pajic.rearm.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ReArmKeybinds {

    private static final KeyMapping ABILITY_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.rearm.ability",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_X,
                    "category.rearm.keybindings"
            )
    );

    private static final KeyMapping BACKSTEP_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.rearm.backstep",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    "category.rearm.keybindings"
            )
    );

    public static void initKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (CooldownTracker.backstepCooldown == 0) {
                if (BackstepAbility.tryBackstep(BACKSTEP_KEY, client)) {
                    CooldownTracker.backstepCooldown = Main.CONFIG.bow.backstepTimeframe();
                }
            }
            if (CooldownTracker.currentAbilityCooldown == 0) {
                AbilityType abilityType = AbilityManagerClient.tryAbilities(ABILITY_KEY, client);
                if (abilityType != AbilityType.NONE) {
                    CooldownTracker.currentAbilityCooldown = abilityType.getCooldown();
                    CooldownTracker.abilityCooldown = abilityType.getCooldown();
                }
            }
        });
    }
}
