package me.pajic.rearm.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Hud.HeartType.class)
public class HeartTypeMixin {

    @ModifyReturnValue(
            method = "forPlayer",
            at = @At("RETURN")
    )
    private static Hud.HeartType checkIfBleeding(
			Hud.HeartType original,
			@Local(argsOnly = true, name = "player") Player player
	) {
        if (original == Hud.HeartType.NORMAL && player.hasEffect(ReArmEffects.BLEEDING)) {
            return Hud.HeartType.valueOf("REARM_BLEEDING");
        }
        return original;
    }
}
