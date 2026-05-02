package me.pajic.rearm.mixin.client;

//? fabric {

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Gui.HeartType.class)
public enum HeartTypeEnumExtensionMixin {
	REARM_BLEEDING(
			ReArm.id("hud/heart/bleeding_full"), ReArm.id("hud/heart/bleeding_full_blinking"),
			ReArm.id("hud/heart/bleeding_half"), ReArm.id("hud/heart/bleeding_half_blinking"),
			ReArm.id("hud/heart/bleeding_hardcore_full"), ReArm.id("hud/heart/bleeding_hardcore_full_blinking"),
			ReArm.id("hud/heart/bleeding_hardcore_half"), ReArm.id("hud/heart/bleeding_hardcore_half_blinking")
	);

	@Shadow
	HeartTypeEnumExtensionMixin(
			Identifier full, Identifier fullBlinking,
			Identifier half, Identifier halfBlinking,
			Identifier hardcoreFull, Identifier hardcoreFullBlinking,
			Identifier hardcoreHalf, Identifier hardcoreHalfBlinking
	) {}
}
//?}
