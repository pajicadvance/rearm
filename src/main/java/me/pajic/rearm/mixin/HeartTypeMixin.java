package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.HeartType.class)
public class HeartTypeMixin {

    @ModifyReturnValue(
            method = "forPlayer",
            at = @At("RETURN")
    )
    private static Gui.HeartType checkIfBleeding(Gui.HeartType original, @Local(argsOnly = true) Player player) {
        if (original == Gui.HeartType.NORMAL && player.hasEffect(ReArmEffects.BLEEDING)) {
            return Gui.HeartType.valueOf("BLEEDING");
        }
        return original;
    }
}
