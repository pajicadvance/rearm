package me.pajic.rearm.mixin;

import me.pajic.rearm.ability.CriticalCounterAbility;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {

    @Inject(
            method = "hurtBlockingItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"
            )
    )
    private void criticalCounter_startTimer(Level level, ItemStack stack, LivingEntity entity, InteractionHand hand, float damage, CallbackInfo ci) {
        if (CriticalCounterAbility.canCounter(entity.getWeaponItem())) {
            ServerPlayNetworking.send((ServerPlayer) entity, new CriticalCounterAbility.S2CStartCriticalCounterTimer());
        }
    }
}
