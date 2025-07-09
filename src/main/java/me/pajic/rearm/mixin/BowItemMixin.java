package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import me.pajic.rearm.Main;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if 1.21.1 {
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
//?}
//? if 1.21.7
/*import net.minecraft.world.InteractionResult;*/

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {

    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"
            ),
            index = 7
    )
    private boolean modifyIsCrit(
            boolean isCrit,
            @Share("isCrit") LocalBooleanRef isPerfectShot,
            @Local(ordinal = 1) int i
    ) {
        if (Main.CONFIG.bow.enablePerfectShot.get()) {
            if (i >= 20 && i <= 20 + Main.CONFIG.bow.perfectShotTimeframe.get() * 20) {
                isPerfectShot.set(true);
                return true;
            }
            return false;
        }
        return isCrit;
    }

    @ModifyArg(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    //? if 1.21.1
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
                    //? if 1.21.7
                    /*target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"*/
            ),
            index = 7
    )
    private float increasePitchIfPerfectShot(float pitch, @Share("isCrit") LocalBooleanRef isPerfectShot) {
        if (isPerfectShot.get()) {
            return pitch * 1.4F;
        }
        return pitch;
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;startUsingItem(Lnet/minecraft/world/InteractionHand;)V"
            )
    )
    private void playPlayerBowDrawingSound(Level level, Player player, InteractionHand interactionHand,
                                           //? if 1.21.1
                                           CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
                                           //? if 1.21.7
                                           /*CallbackInfoReturnable<InteractionResult> cir*/
    ) {
        if (Main.CONFIG.bow.playerDrawingSounds.get() && !player.getProjectile(player.getItemInHand(interactionHand)).isEmpty()) {
            level.playSound(
                    null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.CROSSBOW_QUICK_CHARGE_1,
                    SoundSource.PLAYERS, 0.65F, 1.0F
            );
        }
    }
}
