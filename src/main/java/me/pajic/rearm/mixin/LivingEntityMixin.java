package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.ModTags;
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
import net.minecraft.world.item.component.BlocksAttacks;
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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private int rearm$parryTimer = 0;
    @Unique private final LivingEntity rearm$self = (LivingEntity) (Object) this;

    @ModifyArg(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"
            ),
            index = 3
    )
    private float crossbow_pierceArmor(float original, @Local(argsOnly = true, name = "damageSource") DamageSource damageSource) {
        if (ReArm.CONFIG.crossbow.improvedPiercing.get()) {
            int piercingLevel = damageSource.getWeaponItem() != null ?
                    EnchantmentHelper.getItemEnchantmentLevel(
                            registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.PIERCING),
                            damageSource.getWeaponItem()
                    ) : 0;
            return original * (1 - ((float) (ReArm.CONFIG.crossbow.percentArmorIgnoredPerLevel.get() * piercingLevel) / 100));
        }
        return original;
    }

    @Inject(
            method = "applyItemBlocking",
            at = @At(
                    value = "INVOKE",
					//? if fabric
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;hurtBlockingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;F)V"
					//? if neoforge
					//target = "Lnet/neoforged/neoforge/common/CommonHooks;onDamageBlock(Lnet/minecraft/world/entity/LivingEntity;Lnet/neoforged/neoforge/common/damagesource/DamageContainer;FZ)Lnet/neoforged/neoforge/event/entity/living/LivingShieldBlockEvent;"
            )
    )
    private void parry_onHurtShield(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        if (rearm$self instanceof Player && rearm$parryTimer > 0 && source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() instanceof Projectile projectile) {
            level.sendParticles(ParticleTypes.CRIT, projectile.getX(), projectile.getY(), projectile.getZ(), 8, 0.2, 0.2, 0.2, 0.2);
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(7.5));
            float f = 170.0F + random.nextFloat() * 20.0F;
            projectile.setYRot(projectile.getYRot() + f);
            projectile.needsSync = true;
        }
    }

    @Inject(
            method = "startUsingItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"
            )
    )
    private void parry_onStartUsingShield(InteractionHand hand, CallbackInfo ci, @Local(name = "itemStack") ItemStack itemStack) {
        if (ReArm.CONFIG.shield.enableParry.get() && rearm$self instanceof Player && itemStack.is(ModTags.SHIELDS)) {
            rearm$parryTimer = ReArm.CONFIG.shield.parryTimeframe.get();
        }
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void parry_onTick(CallbackInfo ci) {
        if (rearm$self instanceof Player && rearm$parryTimer > 0) rearm$parryTimer--;
    }

    @WrapWithCondition(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;onBlocked(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    private boolean parry_increasePitch(BlocksAttacks instance, ServerLevel level, LivingEntity user) {
        if (rearm$self instanceof Player && rearm$parryTimer > 0) {
            instance.blockSound().ifPresent(holder -> level.playSound(
                    null, user.getX(), user.getY(), user.getZ(),
                    holder, user.getSoundSource(), 1.0F, 1.2F + level.getRandom().nextFloat() * 0.4F
            ));
            return false;
        }
        return true;
    }

    @ModifyExpressionValue(
            method = "getItemBlockingWith",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/component/BlocksAttacks;blockDelayTicks()I"
            )
    )
    private int parry_removeShieldBlockDelay(int original) {
        return ReArm.CONFIG.bugFixes.shieldDelayFix.get() ? 0 : original;
    }

    @ModifyExpressionValue(
            method = "getVisibilityPercent",
            at = @At(
                    value = "CONSTANT",
                    args = "doubleValue=0.8"
            )
    )
    private double modifyVisibilityWhenDiscrete(double original) {
        if (ReArm.CONFIG.tweaks.improvedSneaking.get()) {
            return 1 - (double) ReArm.CONFIG.tweaks.detectionRangeReduction.get() / 100;
        }
        return original;
    }
}
