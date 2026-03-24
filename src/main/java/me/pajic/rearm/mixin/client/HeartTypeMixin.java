package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Gui.HeartType.class)
public class HeartTypeMixin {

	@Shadow @Final @Mutable private static Gui.HeartType[] $VALUES;

	@SuppressWarnings("SameParameterValue")
	@Invoker("<init>")
	static Gui.HeartType rearm$newHeartType(
			String name,
			int ordinal,
			Identifier full,
			Identifier fullBlinking,
			Identifier half,
			Identifier halfBlinking,
			Identifier hardcoreFull,
			Identifier hardcoreFullBlinking,
			Identifier hardcoreHalf,
			Identifier hardcoreHalfBlinking
	) {
		throw new AssertionError();
	}

	static {
		ArrayList<Gui.HeartType> list = new ArrayList<>(Arrays.asList($VALUES));
		int size = list.size();
		list.add(rearm$newHeartType("REARM_BLEEDING", size,
				ReArm.id("hud/heart/bleeding_full"),
				ReArm.id("hud/heart/bleeding_full_blinking"),
				ReArm.id("hud/heart/bleeding_half"),
				ReArm.id("hud/heart/bleeding_half_blinking"),
				ReArm.id("hud/heart/bleeding_hardcore_full"),
				ReArm.id("hud/heart/bleeding_hardcore_full_blinking"),
				ReArm.id("hud/heart/bleeding_hardcore_half"),
				ReArm.id("hud/heart/bleeding_hardcore_half_blinking"))
		);
		$VALUES = list.toArray(new Gui.HeartType[size + 1]);
	}

    @ModifyReturnValue(
            method = "forPlayer",
            at = @At("RETURN")
    )
    private static Gui.HeartType checkIfBleeding(Gui.HeartType original, @Local(argsOnly = true) Player player) {
        if (original == Gui.HeartType.NORMAL && player.hasEffect(ReArmEffects.BLEEDING)) {
            return Gui.HeartType.valueOf("REARM_BLEEDING");
        }
        return original;
    }
}
