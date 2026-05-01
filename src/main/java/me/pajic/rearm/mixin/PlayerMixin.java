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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(value = Player.class, priority = 250)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("resource")
	//? if neoforge {
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
            method = "doSweepAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            )
    )
	//?}
    private AABB sweepingEdge_increaseAttackRadius(AABB original) {
        if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
            int sweepingEdgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SWEEPING_EDGE),
                    getWeaponItem()
            );
            if (sweepingEdgeLevel > 0) return original.inflate(sweepingEdgeLevel, (double) sweepingEdgeLevel / 4, sweepingEdgeLevel);
        }
		return original;
	}

    @ModifyExpressionValue(
			/*? if fabric {*/method = "doSweepAttack"/*?} else {*//*method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V"*//*?}*/,
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
			/*? if fabric {*/method = "doSweepAttack"/*?} else {*//*method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V"*//*?}*/,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 2
    )
    private <T extends Entity> float sweepingEdge_increaseDamage(
            float damage, @Share("original") LocalRef<List<T>> hitEntityList, @Local(name = "sweepDamage") float sweepDamage
    ) {
        if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
            float additionalDamage = ReArm.CONFIG.sword.sweepingEdgeAdditionalDamagePerMob.get() *
                    Math.min(hitEntityList.get().size() - 1, ReArm.CONFIG.sword.maxMobAmountUsedForDamageIncrease.get());
            return damage + additionalDamage * sweepDamage;
        }
        return damage;
    }

    @ModifyVariable(
			method = "attack",
			at = @At("STORE"),
			name = "criticalAttack"
	)
    private boolean criticalCounter_critOnlyIfCriticalCounter(boolean criticalAttack) {
        if (
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                CriticalCounterAbility.canCounter(getWeaponItem())
        ) {
            return CriticalCounterAbility.getPlayerCounterCondition(serverPlayer.getUUID()) ||
					(CriticalCounterAbility.canVanillaCrit(getWeaponItem()) && criticalAttack);
        }
        return criticalAttack;
    }

    @SuppressWarnings("resource")
    @ModifyExpressionValue(
            method = "getProjectile",
            at = @At(
                    //? if neoforge {
                    /*value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
                    *///?} else {
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z"
                    //?}
            )
    )
    private boolean infinityFix(boolean original, @Local(argsOnly = true, name = "heldWeapon") ItemStack heldWeapon) {
        if (ReArm.CONFIG.tweaks.infinityFix.get()) {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY),
					heldWeapon
            );
            return original || infinityLevel > 0;
        }
        return original;
    }
}
