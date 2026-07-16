package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import me.pajic.rearm.extension.AbstractArrowExtension;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile implements AbstractArrowExtension {

    public AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean isCritArrow();
    @Shadow public abstract ItemStack getWeaponItem();
    @Shadow private @Nullable ItemStack firedFromWeapon;

	@Unique private boolean isMultishotArrow = false;

    @Unique private boolean rearm$shotFromCrossbow() {
        return firedFromWeapon != null && firedFromWeapon.getItem() instanceof CrossbowItem;
    }

	@Override
	public boolean rearm$isMultishotArrow() {
		return isMultishotArrow;
	}

	@Inject(
			method = "<init>(Lnet/minecraft/world/entity/EntityType;DDDLnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("TAIL")
	)
	private void setMultishotArrow(
			EntityType<? extends AbstractArrow> type,
			double x,
			double y,
			double z,
			Level level,
			ItemStack pickupItemStack,
			ItemStack firedFromWeapon,
			CallbackInfo ci
	) {
		if (EnchantmentHelper.getItemEnchantmentLevel(
				level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT),
				firedFromWeapon
		) > 0) isMultishotArrow = true;
	}

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    private double modifyVelocity(double original) {
        if (ReArm.CONFIG.bow.enablePerfectShot.get() && !rearm$shotFromCrossbow() && original > 3.0 && original < 3.5) {
            return 3.0;
        }
        if (ReArm.CONFIG.crossbow.fixedArrowDamage.get() && rearm$shotFromCrossbow()) {
            return ReArm.CONFIG.crossbow.fixedArrowDamageAmount.get() / 2.0;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
					target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;isCritArrow()Z"
            )
    )
    private boolean modifyCrit(
            boolean original,
            @Local(name = "damage") LocalIntRef damage,
            @Local(name = "entity") Entity entity,
            @Local(name = "damageSource") DamageSource damageSource
    ) {
        if (ReArm.CONFIG.bow.enablePerfectShot.get() && isCritArrow() && !rearm$shotFromCrossbow()) {
            if (getWeaponItem() != null) {
                float bonusDamage = EnchantmentHelper.modifyDamage(
                        (ServerLevel) level(),
                        getWeaponItem(),
                        entity,
                        damageSource,
                        ReArm.CONFIG.bow.perfectShotAdditionalDamage.get()
                );
				damage.set((int) (damage.get() + bonusDamage));
            }
            return false;
        }
        if (ReArm.CONFIG.crossbow.fixedArrowDamage.get() && rearm$shotFromCrossbow()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
					target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;getPierceLevel()B",
                    ordinal = 3
            )
    )
    private byte stopPiercingArrowOnArmoredEntity(byte original, @Local(argsOnly = true, name = "hitResult") EntityHitResult hitResult) {
        if (ReArm.CONFIG.crossbow.stopPiercingOnArmoredEntity.get() && original > 0 && hitResult.getEntity() instanceof LivingEntity entity) {
            if (
                    !entity.equipment.get(EquipmentSlot.HEAD).isEmpty() ||
                    !entity.equipment.get(EquipmentSlot.CHEST).isEmpty() ||
                    !entity.equipment.get(EquipmentSlot.LEGS).isEmpty() ||
                    !entity.equipment.get(EquipmentSlot.FEET).isEmpty()
            ) {
                return 0;
            }
        }
        return original;
    }
}
