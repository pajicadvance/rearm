package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CriticalCounterAbility;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
//? if 1.21.1 {
/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
*///?}

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(value = Player.class, priority = 250)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("resource")
	//? if >= 1.21.11 && neoforge {
	/*@ModifyArg(
			method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
			),
			index = 1
	)
	*///?} else {
    @ModifyExpressionValue(
            /*? if < 1.21.11 {*//*method = "attack"*//*?} else if fabric {*/method = "doSweepAttack"/*?} else {*//*method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V"*//*?}*/,
            at = @At(
                    value = "INVOKE",
					//? if fabric
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
					//? if neoforge
					//target = "Lnet/minecraft/world/item/ItemStack;getSweepHitBox(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/AABB;"
            )
    )
	//?}
    private AABB sweepingEdge_increaseAttackRadius(AABB original) {
        if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
            int sweepingEdgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    //? if 1.21.1
                    //level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SWEEPING_EDGE),
                    //? if > 1.21.1
                    level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SWEEPING_EDGE),
                    getWeaponItem()
            );
            if (sweepingEdgeLevel > 0) return original.inflate(sweepingEdgeLevel, (double) sweepingEdgeLevel / 4, sweepingEdgeLevel);
        }
		return original;
	}

    @ModifyExpressionValue(
			/*? if < 1.21.11 {*//*method = "attack"*//*?} else if fabric {*/method = "doSweepAttack"/*?} else {*//*method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V"*//*?}*/,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private <T extends Entity> List<T> sweepingEdge_getHitEntities(
            List<T> original, @Share("original") LocalRef<List<T>> hitEntityList
    ) {
        if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
            hitEntityList.set(original);
        }
        return original;
    }

    @ModifyArg(
			/*? if < 1.21.11 {*//*method = "attack"*//*?} else if fabric {*/method = "doSweepAttack"/*?} else {*//*method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V"*//*?}*/,
            at = @At(
                    value = "INVOKE",
                    //? if 1.21.1
                    //target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
                    //? if > 1.21.1
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = /*? if 1.21.1 {*//*1*//*?} else {*/2/*?}*/
    )
    private <T extends Entity> float sweepingEdge_increaseDamage(
            float damage, @Share("original") LocalRef<List<T>> hitEntityList, @Local(ordinal = 2) float h
    ) {
        if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
            float additionalDamage = ReArm.CONFIG.sword.sweepingEdgeAdditionalDamagePerMob.get() *
                    Math.min(hitEntityList.get().size() - 1, ReArm.CONFIG.sword.maxMobAmountUsedForDamageIncrease.get());
            return damage + additionalDamage * h;
        }
        return damage;
    }

	@SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 2
    )
    private boolean criticalCounter_critOnlyIfCriticalCounter(boolean original) {
        if (
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                CriticalCounterAbility.canCounter(getWeaponItem())
        ) {
            return CriticalCounterAbility.getPlayerCounterCondition(serverPlayer.getUUID()) ||
					(CriticalCounterAbility.canVanillaCrit(getWeaponItem()) && original);
        }
        return original;
    }

    //? if 1.21.1 {
    /*@WrapMethod(method = "hurtCurrentlyUsedShield")
    private void criticalCounter_startTimer(float damageAmount, Operation<Void> original) {
        original.call(damageAmount);
        if (
                !useItem.isEmpty() &&
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                CriticalCounterAbility.canCounter(getWeaponItem())
        ) {
            ReArm.xplat().sendToClient(serverPlayer, new CriticalCounterAbility.S2CStartCriticalCounterTimer());
        }
    }
    *///?}

    @SuppressWarnings("resource")
    @ModifyExpressionValue(
            method = "getProjectile",
            at = @At(
                    //? if (fabric && 1.21.1) || neoforge {
                    /*value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
                    *///?}
                    //? if fabric && >= 1.21.7 {
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"
                    //?}
            )
    )
    private boolean infinityFix(boolean original, @Local(argsOnly = true) ItemStack weaponStack) {
        if (ReArm.CONFIG.tweaks.infinityFix.get()) {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    //? if 1.21.1
                    //level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.INFINITY),
                    //? if >= 1.21.7
                    level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY),
                    weaponStack
            );
            return original || infinityLevel > 0;
        }
        return original;
    }
}
