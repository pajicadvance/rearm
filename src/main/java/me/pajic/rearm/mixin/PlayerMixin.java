package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.AbilityManager;
import me.pajic.rearm.effect.ReArmEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
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

@Mixin(Player.class)
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
        if (Main.CONFIG.sweepingEdge.sweepingEdgeAbility()) {
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
        if (Main.CONFIG.sweepingEdge.sweepingEdgeAbility()) {
            if (AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)) {
                return original;
            }
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
            args.set(0, Main.CONFIG.sweepingEdge.sweepingEdgeRange.x());
            args.set(1, Main.CONFIG.sweepingEdge.sweepingEdgeRange.y());
            args.set(2, Main.CONFIG.sweepingEdge.sweepingEdgeRange.z());
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
            float damage, @Share("original") LocalRef<List<T>> hitEntityList
    ) {
        if (
                Main.CONFIG.sweepingEdge.sweepingEdgeAbility() &&
                AbilityManager.sweepingEdgeAbility.shouldTriggerAbility(getWeaponItem(), (Player) (Object) this)
        ) {
            return damage + Main.CONFIG.sweepingEdge.sweepingEdgeAdditionalDamagePerMob() * hitEntityList.get().size();
        }
        return damage;
    }

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;sweepAttack()V"
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
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 5), serverPlayer);
                livingEntity.addEffect(new MobEffectInstance(ReArmEffects.BLEEDING, 120, 1), serverPlayer);
                AbilityManager.setPlayerAbilityUsed(serverPlayer);
            }
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

    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 2
    )
    private boolean modifyCritCondition(boolean original) {
        if (Main.CONFIG.sword.disableCriticalHits() && getWeaponItem().is(ItemTags.SWORDS)) {
            return false;
        }
        return original;
    }

    @WrapWithCondition(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;crit(Lnet/minecraft/world/entity/Entity;)V"
            )
    )
    private boolean onlyCritIfAllowed(Player instance, Entity entityHit) {
        return !Main.CONFIG.sword.disableCriticalHits() || !getWeaponItem().is(ItemTags.SWORDS);
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
}
