package me.pajic.rearm.projectile;

import me.pajic.rearm.Main;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class ThrownAxe extends AbstractArrow {
    private boolean dealtDamage;
    private LivingEntity stuckEntity;
    private UUID stuckEntityId;
    private float damage;
    public static final EntityDataAccessor<Boolean> STUCK = SynchedEntityData.defineId(ThrownAxe.class, EntityDataSerializers.BOOLEAN);

    public ThrownAxe(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownAxe(Level level, LivingEntity shooter, ItemStack axe, float damage) {
        super(CripplingThrowAbility.AXE, shooter, level, axe, axe);
        setAttached(CripplingThrowAbility.THROWN_AXE_ITEM_STACK, axe);
        entityData.set(STUCK, false);
        CripplingThrowAbility.recallSignals.remove(shooter.getUUID());
        this.damage = damage;
    }

    @Override
    public void tick() {
        if (stuckEntity == null && level() instanceof ServerLevel sl && stuckEntityId != null && !stuckEntityId.equals(new UUID(0, 0))) {
            stuckEntity = (LivingEntity) sl.getEntity(stuckEntityId);
            setNoGravity(true);
            entityData.set(STUCK, true);
        }
        if (inGroundTime > 4) dealtDamage = true;
        if (inGround) entityData.set(STUCK, true);

        Entity entity = getOwner();
        if (entity != null) {
            if (((dealtDamage || isNoPhysics()) && CripplingThrowAbility.recallSignals.contains(entity.getUUID())) || getY() < -65) {
                if (stuckEntity != null && !stuckEntity.getType().is(EntityTypeTags.SKELETONS)) {
                    stuckEntity.addEffect(
                            new MobEffectInstance(
                                    ReArmEffects.BLEEDING,
                                    Main.CONFIG.axe.cripplingThrowBleedingDuration(),
                                    getCripplingThrowLevel()
                            ), entity
                    );
                    playSound(SoundEvents.HOSTILE_HURT, 1.0F, 1.0F);
                }
                if (!isAcceptableReturnOwner()) {
                    if (!level().isClientSide && pickup == AbstractArrow.Pickup.ALLOWED) {
                        spawnAtLocation(getPickupItem(), 0.1F);
                    }
                    discard();
                } else {
                    setNoPhysics(true);
                    Vec3 vec3 = entity.getEyePosition().subtract(position());
                    setPosRaw(getX(), getY() + vec3.y * 0.045, getZ());
                    if (level().isClientSide) {
                        yOld = getY();
                    }
                    setDeltaMovement(getDeltaMovement().scale(0.95).add(vec3.normalize().scale(0.15)));
                }
                stuckEntity = null;
                setNoGravity(false);
                entityData.set(STUCK, false);
            }
        }

        if (stuckEntity != null) {
            setPos(stuckEntity.getX(), stuckEntity.getY() + stuckEntity.getBbHeight() / 2, stuckEntity.getZ());
            stuckEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN,
                            20,
                            Main.CONFIG.axe.cripplingThrowBaseSlownessAmplifier() +
                                    (getCripplingThrowLevel() - 1) * Main.CONFIG.axe.cripplingThrowSlownessAmplifierIncreasePerLevel()
                    ), entity
            );
            if (!stuckEntity.isAlive()) {
                stuckEntity = null;
                setNoGravity(false);
                entityData.set(STUCK, false);
            }
        }

        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity entity = getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity2 = getOwner();
        DamageSource damageSource = damageSources().thrown(this, entity2 == null ? this : entity2);
        float f = damage;
        float g = 0;
        if (level() instanceof ServerLevel serverLevel) {
            g = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), entity, damageSource, f) - f;
        }

        dealtDamage = true;
        if (entity.hurt(damageSource, f + g)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, getWeaponItem());
            }
            if (entity instanceof LivingEntity livingEntity) {
                doKnockback(livingEntity, damageSource);
                doPostHurtEffects(livingEntity);
                stuckEntity = livingEntity;
                setNoGravity(true);
                entityData.set(STUCK, true);
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(0.01, 0.1, 0.01));
        playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    private int getCripplingThrowLevel() {
        return EnchantmentHelper.getItemEnchantmentLevel(
                registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.CRIPPLING_THROW),
                getAttachedOrElse(CripplingThrowAbility.THROWN_AXE_ITEM_STACK, ItemStack.EMPTY)
        );
    }

    @Override
    protected void hitBlockEnchantmentEffects(@NotNull ServerLevel level, BlockHitResult hitResult, @NotNull ItemStack stack) {
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        EnchantmentHelper.onHitBlock(
                level,
                stack,
                this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null,
                this,
                null,
                vec3,
                level.getBlockState(hitResult.getBlockPos()),
                item -> this.kill()
        );
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STUCK, false);
    }

    @Override
    protected EntityHitResult findHitEntity(@NotNull Vec3 startVec, @NotNull Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    public @NotNull ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        boolean result = super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
        if (result) CripplingThrowAbility.recallSignals.remove(player.getUUID());
        return result;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        dealtDamage = compound.getBoolean("DealtDamage");
        stuckEntityId = compound.getUUID("StuckEntityId");
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", dealtDamage);
        compound.putUUID("StuckEntityId", stuckEntity == null ? new UUID(0, 0) : stuckEntity.getUUID());
    }

    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.8F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
