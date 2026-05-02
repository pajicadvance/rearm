package me.pajic.rearm.projectile;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ThrownAxe extends AbstractArrow {

    private boolean dealtDamage;
    private boolean failedPickup;
    private LivingEntity stuckEntity;
    private UUID stuckEntityId;
    private float damage;
    private InteractionHand hand;
    private int timeInTarget;
    public static final EntityDataAccessor<Boolean> STUCK = SynchedEntityData.defineId(ThrownAxe.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ALLOW_PICKUP = SynchedEntityData.defineId(ThrownAxe.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> THROWN_AXE_ITEM_STACK = SynchedEntityData.defineId(ThrownAxe.class, EntityDataSerializers.ITEM_STACK);

    public ThrownAxe(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownAxe(Level level, LivingEntity shooter, ItemStack axe, float damage, InteractionHand hand) {
        super(CripplingThrowAbility.AXE, shooter, level, axe, axe);
        entityData.set(STUCK, false);
        entityData.set(ALLOW_PICKUP, false);
        entityData.set(THROWN_AXE_ITEM_STACK, axe);
        CripplingThrowAbility.recallSignals.remove(shooter.getUUID());
        this.damage = damage;
        this.hand = hand;
    }

    @SuppressWarnings("resource")
    @Override
    public void tick() {
        if (stuckEntity == null && level() instanceof ServerLevel sl && stuckEntityId != null && !stuckEntityId.equals(new UUID(0, 0))) {
            stuckEntity = (LivingEntity) sl.getEntity(stuckEntityId);
            setNoGravity(true);
            entityData.set(STUCK, true);
        }
        if (inGroundTime > 4) dealtDamage = true;
        if (isInGround()) {
            entityData.set(STUCK, true);
            entityData.set(ALLOW_PICKUP, true);
        }

        Entity entity = getOwner();
        if (entity != null && checkLoyalty()) {
            if (((dealtDamage || isNoPhysics()) && (CripplingThrowAbility.recallSignals.contains(entity.getUUID())) || getY() < -65) && !failedPickup) {
                applyBleedingToStuckEntity(entity);
                if (!isAcceptableReturnOwner()) {
                    if (!level().isClientSide() && pickup == Pickup.ALLOWED) {
                        spawnAtLocation((ServerLevel) level(), getPickupItem(), 0.1F);
                    }
                    discard();
                } else {
                    setNoPhysics(true);
                    entityData.set(ALLOW_PICKUP, true);
                    Vec3 vec3 = entity.getEyePosition().subtract(position());
                    setPosRaw(getX(), getY() + vec3.y * 0.045, getZ());
                    if (level().isClientSide()) {
                        yOld = getY();
                    }
                    setDeltaMovement(getDeltaMovement().scale(0.95).add(vec3.normalize().scale(0.15)));
                }
                stuckEntity = null;
                stuckEntityId = null;
                setNoGravity(false);
                entityData.set(STUCK, false);
                timeInTarget = 0;
            }
        }

        if (stuckEntity != null) {
            setPos(stuckEntity.getX(), stuckEntity.getY() + stuckEntity.getBbHeight() / 2, stuckEntity.getZ());
            stuckEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.SLOWNESS,
                            20,
							ReArm.CONFIG.axe.cripplingThrowBaseSlownessAmplifier.get() +
                                    (getCripplingThrowLevel() - 1) * ReArm.CONFIG.axe.cripplingThrowSlownessAmplifierIncreasePerLevel.get()
                    ), entity
            );
            timeInTarget++;
            if (!stuckEntity.isAlive() || timeInTarget > ReArm.CONFIG.axe.maxTimeStuckInTarget.get()) {
				if (stuckEntity.isAlive()) applyBleedingToStuckEntity(entity);
                stuckEntity = null;
                stuckEntityId = null;
                setNoGravity(false);
                entityData.set(STUCK, false);
                timeInTarget = 0;
            }
        }

        super.tick();
    }

	private boolean checkLoyalty() {
		if (ReArm.CONFIG.axe.requireLoyaltyForRecall.get()) return EnchantmentHelper.getItemEnchantmentLevel(
				registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOYALTY),
				entityData.get(THROWN_AXE_ITEM_STACK)
		) > 0;
		return true;
	}

	@SuppressWarnings("deprecation")
	private void applyBleedingToStuckEntity(Entity dealer) {
		if (stuckEntity != null && !stuckEntity.getType().builtInRegistryHolder().is(EntityTypeTags.SKELETONS)) {
			stuckEntity.addEffect(
					new MobEffectInstance(
							ReArmEffects.BLEEDING,
							ReArm.CONFIG.axe.cripplingThrowBleedingDuration.get(),
							getCripplingThrowLevel()
					), dealer
			);
			playSound(SoundEvents.HOSTILE_HURT, 1.0F, 1.0F);
		}
	}

    private boolean isAcceptableReturnOwner() {
        Entity entity = getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    @SuppressWarnings({"resource", "deprecation"})
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
        if (entity.hurtOrSimulate(damageSource, f + g)) {
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
                stuckEntityId = livingEntity.getUUID();
                setNoGravity(true);
                entityData.set(STUCK, true);
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(0.01, 0.1, 0.01));
        playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    private int getCripplingThrowLevel() {
        return EnchantmentHelper.getItemEnchantmentLevel(
                registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.CRIPPLING_THROW),
                entityData.get(THROWN_AXE_ITEM_STACK)
        );
    }

    @Override
    protected void hitBlockEnchantmentEffects(@NotNull ServerLevel level, BlockHitResult hitResult, @NotNull ItemStack stack) {
        Vec3 vec3 = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        EnchantmentHelper.onHitBlock(
                level,
                stack,
                getOwner() instanceof LivingEntity livingEntity ? livingEntity : null,
                this,
                null,
                vec3,
                level.getBlockState(hitResult.getBlockPos()),
				_ -> kill(level)
        );
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STUCK, false);
        builder.define(ALLOW_PICKUP, false);
        builder.define(THROWN_AXE_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    protected EntityHitResult findHitEntity(@NotNull Vec3 startVec, @NotNull Vec3 endVec) {
        return dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    public @NotNull ItemStack getWeaponItem() {
        return getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(@NotNull Player player) {
        boolean result = switch (pickup) {
            case DISALLOWED -> false;
            case ALLOWED -> entityData.get(ALLOW_PICKUP) && ownedBy(player) && !player.hasInfiniteMaterials();
            case CREATIVE_ONLY -> player.hasInfiniteMaterials();
        };
        if (result) {
			if (!player.hasInfiniteMaterials()) {
		        if (hand != null && player.getItemInHand(hand).isEmpty()) {
					player.setItemInHand(hand, getPickupItem());
				} else {
					boolean added = player.getInventory().add(getPickupItem());
					if (!added) {
						failedPickup = true;
						stuckEntity = null;
						setNoGravity(false);
						setNoPhysics(false);
						entityData.set(STUCK, false);
						timeInTarget = 0;
						return false;
					}
				}
	        }
            CripplingThrowAbility.recallSignals.remove(player.getUUID());
        }
        return result;
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @SuppressWarnings("resource")
    @Override
    public void playerTouch(@NotNull Player player) {
        if (ownedBy(player) || getOwner() == null && !level().isClientSide() && (isInGround() || isNoPhysics()) && shakeTime <= 0) {
            if (tryPickup(player)) {
                player.take(this, 1);
                discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        dealtDamage = valueInput.getBooleanOr("DealtDamage", false);
        failedPickup = valueInput.getBooleanOr("FailedPickup", false);
        stuckEntityId = valueInput.read("StuckEntityId", UUIDUtil.CODEC).orElse(null);
        timeInTarget = valueInput.getIntOr("TimeInTarget", 0);
        hand = valueInput.getBooleanOr("Hand", true) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        entityData.set(STUCK, valueInput.getBooleanOr("Stuck", false));
        entityData.set(ALLOW_PICKUP, valueInput.getBooleanOr("AllowPickup", true));
        entityData.set(THROWN_AXE_ITEM_STACK, valueInput.read("ThrownAxeItemStack", ItemStack.CODEC).orElse(ItemStack.EMPTY));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putBoolean("DealtDamage", dealtDamage);
        valueOutput.putBoolean("FailedPickup", failedPickup);
        if (stuckEntityId != null) {
            valueOutput.store("StuckEntityId", UUIDUtil.CODEC, stuckEntityId);
        }
        valueOutput.putInt("TimeInTarget", timeInTarget);
        valueOutput.putBoolean("Hand", hand == InteractionHand.MAIN_HAND);
        valueOutput.putBoolean("Stuck", entityData.get(STUCK));
        valueOutput.putBoolean("AllowPickup", entityData.get(ALLOW_PICKUP));
        if (!entityData.get(THROWN_AXE_ITEM_STACK).isEmpty()) {
            valueOutput.store("ThrownAxeItemStack", ItemStack.CODEC, entityData.get(THROWN_AXE_ITEM_STACK));
        }
    }

	@Override
    public void tickDespawn() {
        if (pickup != Pickup.ALLOWED) {
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
