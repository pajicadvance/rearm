package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.AbilityManager;
import me.pajic.rearm.ability.CriticalCounterManager;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(value = Player.class, priority = 250)
public abstract class PlayerMixin extends LivingEntity {

    @Shadow public abstract @NotNull ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D",
                    ordinal = 1
            )
    )
    private double sweepingEdgeAbility_removeVanillaDamage(double original) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                !AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)
        ) {
            return 0;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getEnchantedDamage(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;)F",
                    ordinal = 1
            )
    )
    private float sweepingEdgeAbility_removeEnchantmentDamage(float original) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                !AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)
        ) {
            return 1;
        }
        return original;
    }

    @ModifyArgs(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"
            )
    )
    private void sweepingEdgeAbility_increaseAttackRadius(Args args) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)
        ) {
            int sweepingEdgeLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.SWEEPING_EDGE),
                    getWeaponItem()
            );
            args.set(0, (double) args.get(0) * sweepingEdgeLevel);
            args.set(2, (double) args.get(2) * sweepingEdgeLevel);
            if (sweepingEdgeLevel == 3) {
                args.set(1, 1.0);
            }
        }
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
            )
    )
    private <T extends Entity> List<T> sweepingEdgeAbility_getHitEntities(
            List<T> original, @Share("original") LocalRef<List<T>> hitEntityList
    ) {
        if (Main.CONFIG.sweepingEdge.sweepingEdgeAbility()) {
            hitEntityList.set(original);
        }
        return original;
    }

    @ModifyArg(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            ),
            index = 1
    )
    private <T extends Entity> float sweepingEdgeAbility_increaseDamage(
            float damage, @Share("original") LocalRef<List<T>> hitEntityList, @Local(ordinal = 2) float h
    ) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)
        ) {
            float additionalDamage = Main.CONFIG.sweepingEdge.sweepingEdgeAdditionalDamagePerMob() *
                    Math.min(hitEntityList.get().size() - 1, Main.CONFIG.sweepingEdge.maxMobAmountUsedForDamageIncrease());
            return damage + additionalDamage * h;
        }
        return damage;
    }

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private void sweepingEdgeAbility_abilityUsed(Entity target, CallbackInfo ci) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), serverPlayer)
        ) {
            AbilityManager.setPlayerAbilityUsed(serverPlayer);
        }
    }

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean cripplingBlowAbility_applyStatusEffects(boolean original, @Local(argsOnly = true) Entity target) {
        if (
                Main.CONFIG.cripplingBlow.cripplingBlowAbility() &&
                original && (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                AbilityManager.cripplingBlowAbility.shouldTriggerAbility(getWeaponItem(), serverPlayer)
        ) {
            if (target instanceof LivingEntity livingEntity) {
                int cripplingBlowLevel = EnchantmentHelper.getItemEnchantmentLevel(
                        level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                                .getHolderOrThrow(ReArmEnchantments.CRIPPLING_BLOW),
                        getWeaponItem()
                );
                livingEntity.addEffect(
                        new MobEffectInstance(
                                MobEffects.MOVEMENT_SLOWDOWN,
                                Main.CONFIG.cripplingBlow.cripplingBlowSlownessDuration(),
                                Main.CONFIG.cripplingBlow.cripplingBlowBaseSlownessAmplifier() +
                                        (cripplingBlowLevel - 1) * Main.CONFIG.cripplingBlow.cripplingBlowSlownessAmplifierIncreasePerLevel()
                        ),
                        serverPlayer
                );
                if (!livingEntity.getType().is(EntityTypeTags.SKELETONS)) {
                    livingEntity.addEffect(
                            new MobEffectInstance(
                                    ReArmEffects.BLEEDING,
                                    Main.CONFIG.cripplingBlow.cripplingBlowBleedingDuration(),
                                    cripplingBlowLevel
                            ),
                            serverPlayer
                    );
                }
                AbilityManager.setPlayerAbilityUsed(serverPlayer);
            }
        }
        return original;
    }

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 2
    )
    private boolean criticalCounter_critOnlyIfCriticalCounter(boolean original) {
        if (
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                Main.CONFIG.sword.enableCriticalCounter() &&
                getWeaponItem().is(ItemTags.SWORDS)
        ) {
            return CriticalCounterManager.getPlayerCounterCondition(serverPlayer.getUUID());
        }
        return original;
    }

    @Inject(
            method = "hurtCurrentlyUsedShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;level()Lnet/minecraft/world/level/Level;",
                    ordinal = 0
            )
    )
    private void criticalCounter_startTimer(float damageAmount, CallbackInfo ci) {
        if (
                (Player) (Object) this instanceof ServerPlayer serverPlayer &&
                Main.CONFIG.sword.enableCriticalCounter() &&
                getWeaponItem().is(ItemTags.SWORDS)
        ) {
            ServerPlayNetworking.send(serverPlayer, new CriticalCounterManager.S2CStartCriticalCounterTimer());
        }
    }

    @ModifyExpressionValue(
            method = "getProjectile",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
            )
    )
    private boolean infinityFix(boolean original, @Local(argsOnly = true) ItemStack weaponStack) {
        if (Main.CONFIG.infinityFix()) {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.INFINITY),
                    weaponStack
            );
            return original || infinityLevel > 0;
        }
        return original;
    }

    @Inject(
            method = "attack",
            at = @At("TAIL")
    )
    private void resetDataAfterUse(Entity target, CallbackInfo ci) {
        if ((Player) (Object) this instanceof ServerPlayer serverPlayer) {
            if (
                    Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                    AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), serverPlayer) ||
                    Main.CONFIG.cripplingBlow.cripplingBlowAbility() &&
                            AbilityManager.cripplingBlowAbility.shouldTriggerAbility(getWeaponItem(), serverPlayer)
            ) {
                AbilityManager.resetPlayerAbilityData(serverPlayer);
            }
        }
    }
}
