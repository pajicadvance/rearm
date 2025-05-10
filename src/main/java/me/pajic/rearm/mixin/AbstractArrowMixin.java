package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {

    public AbstractArrowMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean isCritArrow();
    @Shadow public abstract boolean shotFromCrossbow();
    @Shadow public abstract ItemStack getWeaponItem();

    @ModifyExpressionValue(
            method = "shotFromCrossbow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean considerModCrossbows(boolean original) {
        return ReArmItems.isCrossbow(getWeaponItem());
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    private double modifyVelocity(double original) {
        if (Main.CONFIG.bow.enablePerfectShot() && !shotFromCrossbow() && original > 3.0 && original < 3.5) {
            return 3.0;
        }
        if (Main.CONFIG.crossbow.fixedArrowDamage() && shotFromCrossbow()) {
            return Main.CONFIG.crossbow.fixedArrowDamageAmount() / 2.0;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"
            )
    )
    private boolean modifyCrit(boolean original,
                               @Local LocalIntRef i,
                               @Local(ordinal = 0) Entity entity,
                               @Local DamageSource damageSource
    ) {
        if (Main.CONFIG.bow.enablePerfectShot() && isCritArrow() && !shotFromCrossbow()) {
            if (getWeaponItem() != null) {
                float bonusDamage = EnchantmentHelper.modifyDamage(
                        (ServerLevel) level(),
                        getWeaponItem(),
                        entity,
                        damageSource,
                        Main.CONFIG.bow.perfectShotAdditionalDamage()
                );
                i.set((int) (i.get() + bonusDamage));
            }
            return false;
        }
        if (Main.CONFIG.crossbow.fixedArrowDamage() && shotFromCrossbow()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getPierceLevel()B",
                    ordinal = 3
            )
    )
    private byte stopPiercingArrowOnArmoredEntity(byte original, @Local(argsOnly = true) EntityHitResult result) {
        if (Main.CONFIG.crossbow.stopPiercingOnArmoredEntity() && original > 0 && result.getEntity() instanceof LivingEntity entity) {
            for (ItemStack stack : entity.getArmorSlots()) {
                if (
                        stack.is(ItemTags.HEAD_ARMOR) ||
                        stack.is(ItemTags.CHEST_ARMOR) ||
                        stack.is(ItemTags.LEG_ARMOR) ||
                        stack.is(ItemTags.FOOT_ARMOR)
                ) {
                    return 0;
                }
            }
        }
        return original;
    }
}
