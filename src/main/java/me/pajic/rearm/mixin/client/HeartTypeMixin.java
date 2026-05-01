package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Gui.HeartType.class)
public enum HeartTypeMixin {
	REARM_BLEEDING(
			ReArm.id("hud/heart/bleeding_full"), ReArm.id("hud/heart/bleeding_full_blinking"),
			ReArm.id("hud/heart/bleeding_half"), ReArm.id("hud/heart/bleeding_half_blinking"),
			ReArm.id("hud/heart/bleeding_hardcore_full"), ReArm.id("hud/heart/bleeding_hardcore_full_blinking"),
			ReArm.id("hud/heart/bleeding_hardcore_half"), ReArm.id("hud/heart/bleeding_hardcore_half_blinking")
	);

	@Shadow HeartTypeMixin(
			Identifier full, Identifier fullBlinking,
			Identifier half, Identifier halfBlinking,
			Identifier hardcoreFull, Identifier hardcoreFullBlinking,
			Identifier hardcoreHalf, Identifier hardcoreHalfBlinking
	) {}

    @ModifyReturnValue(
            method = "forPlayer",
            at = @At("RETURN")
    )
    private static Gui.HeartType checkIfBleeding(
			Gui.HeartType original,
			@Local(argsOnly = true, name = "player") Player player
	) {
        if (original == Gui.HeartType.NORMAL && player.hasEffect(ReArmEffects.BLEEDING)) {
            return Gui.HeartType.valueOf("REARM_BLEEDING");
        }
        return original;
    }
}
