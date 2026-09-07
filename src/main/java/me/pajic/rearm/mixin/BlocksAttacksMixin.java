package me.pajic.rearm.mixin;

//? >=26.1 {

import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.platform.MultiLoaderUtil;
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
			//? if fabric
            method = "hurtBlockingItem",
			//? if neoforge
			//method = "hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;FI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"
            )
    )
    private void criticalCounter_startTimer(Level level, ItemStack item, LivingEntity user, InteractionHand hand, float damage, /*? if neoforge {*//*int fixedDamage,*//*?}*/ CallbackInfo ci) {
        if (CriticalCounterAbility.canCounter(user.getWeaponItem())) {
            MultiLoaderUtil.INSTANCE.sendToClient((ServerPlayer) user, new CriticalCounterAbility.S2CStartCriticalCounterTimer());
        }
    }
}
//?}
