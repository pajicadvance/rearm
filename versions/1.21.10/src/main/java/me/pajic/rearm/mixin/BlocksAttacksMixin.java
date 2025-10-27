package me.pajic.rearm.mixin;

import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.network.ReArmNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlocksAttacks.class)
public class BlocksAttacksMixin {

    @Inject(
            method = "hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;FI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"
            )
    )
    private void criticalCounter_startTimer(Level level, ItemStack stack, LivingEntity entity, InteractionHand hand, float damage, int fixedDamage, CallbackInfo ci) {
        if (CriticalCounterAbility.canCounter(entity.getWeaponItem())) {
            PacketDistributor.sendToPlayer((ServerPlayer) entity, new ReArmNetworking.S2CStartCriticalCounterTimer());
        }
    }
}
