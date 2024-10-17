package me.pajic.ranger.mixin;

import me.pajic.ranger.Main;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangedBowAttackGoal.class)
public class RangedBowAttackGoalMixin<T extends Monster & RangedAttackMob> {

    @Shadow
    @Final
    private T mob;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Monster;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"
            )
    )
    private void playMobBowDrawingSound(CallbackInfo ci) {
        if (Main.CONFIG.bow.mobDrawingSounds()) {
            mob.playSound(
                    SoundEvents.CROSSBOW_QUICK_CHARGE_1.value(),
                    0.65F, 1.0F
            );
        }
    }
}
