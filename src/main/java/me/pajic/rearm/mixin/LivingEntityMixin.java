package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if >= 1.21.7
/*import net.minecraft.world.item.component.BlocksAttacks;*/

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private int parryTimer = 0;
    @Unique private final LivingEntity self = (LivingEntity) (Object) this;

    @ModifyArg(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"
            ),
            index = 3
    )
    private float crossbow_pierceArmor(float original, @Local(argsOnly = true) DamageSource source) {
        if (Main.CONFIG.crossbow.improvedPiercing.get()) {
            int piercingLevel = source.getWeaponItem() != null ?
                    EnchantmentHelper.getItemEnchantmentLevel(
                            //? if 1.21.1
                            registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PIERCING),
                            //? if >= 1.21.7
                            /*registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PIERCING),*/
                            source.getWeaponItem()
                    ) : 0;
            return original * (1 - ((float) (Main.CONFIG.crossbow.percentArmorIgnoredPerLevel.get() * piercingLevel) / 100));
        }
        return original;
    }

    //? if < 1.21.7 {
    @Inject(
            method = "hurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"
            )
    )
    private void parry_onHurtShield(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (self instanceof Player && parryTimer > 0 && source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() instanceof Projectile projectile) {
            if (!level().isClientSide) ((ServerLevel) level()).sendParticles(ParticleTypes.CRIT, projectile.getX(), projectile.getY(), projectile.getZ(), 8, 0.2, 0.2, 0.2, 0.2);
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(7.5));
            float f = 170.0F + random.nextFloat() * 20.0F;
            projectile.setYRot(projectile.getYRot() + f);
            projectile.hasImpulse = true;
        }
    }
    //?}
    //? if >= 1.21.7 {
    /*@Inject(
            method = "applyItemBlocking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;F)V"
            )
    )
    private void parry_onHurtShield(ServerLevel level, DamageSource source, float damageAmount, CallbackInfoReturnable<Float> cir) {
        if (self instanceof Player && parryTimer > 0 && source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() instanceof Projectile projectile) {
            level.sendParticles(ParticleTypes.CRIT, projectile.getX(), projectile.getY(), projectile.getZ(), 8, 0.2, 0.2, 0.2, 0.2);
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(7.5));
            float f = 170.0F + random.nextFloat() * 20.0F;
            projectile.setYRot(projectile.getYRot() + f);
            projectile.hasImpulse = true;
        }
    }
    *///?}

    @Inject(
            method = "startUsingItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"
            )
    )
    private void parry_onStartUsingShield(InteractionHand hand, CallbackInfo ci, @Local ItemStack itemStack) {
        if (Main.CONFIG.shield.enableParry.get() && self instanceof Player && itemStack.getItem() instanceof ShieldItem) {
            parryTimer = Main.CONFIG.shield.parryTimeframe.get();
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void parry_onTick(CallbackInfo ci) {
        if (self instanceof Player && parryTimer > 0) parryTimer--;
    }

    //? if < 1.21.7 {
    @ModifyArg(
            method = "handleEntityEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V",
                    ordinal = 2
            ),
            index = 2
    )
    private float parry_increasePitch(float original) {
        if (self instanceof Player && parryTimer > 0) return original + 0.4F;
        return original;
    }
    //?}
    //? if >= 1.21.7 {
    /*@WrapWithCondition(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;onBlocked(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private boolean parry_increasePitch(BlocksAttacks instance, ServerLevel level, LivingEntity entity) {
        if (self instanceof Player && parryTimer > 0) {
            instance.blockSound().ifPresent(holder -> level.playSound(
                    null, entity.getX(), entity.getY(), entity.getZ(),
                    holder, entity.getSoundSource(), 1.0F, 1.2F + level.random.nextFloat() * 0.4F
            ));
            return false;
        }
        return true;
    }
    *///?}

    //? if < 1.21.7 {
    @ModifyExpressionValue(
            method = "isBlocking",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=5"
            )
    )
    private int parry_removeShieldBlockDelay(int original) {
        return 0;
    }
    //?}
    //? if >= 1.21.7 {
    /*@ModifyExpressionValue(
            method = "getItemBlockingWith",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;blockDelayTicks()I"
            )
    )
    private int parry_removeShieldBlockDelay(int original) {
        return 0;
    }
    *///?}
}
