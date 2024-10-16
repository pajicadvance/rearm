package me.pajic.ranger.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.pajic.ranger.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
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
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    private double modifyVelocity(double original) {
        if (Main.CONFIG.bow.enablePerfectShot() && !shotFromCrossbow() && original > 3.0) {
            // Cap bow velocity to 3.0 to prevent damage overshot by one
            // e.g. int i = 2 * 3.01 results in 7
            return 3.0;
        }
        if (Main.CONFIG.crossbow.fixedArrowDamage() && shotFromCrossbow()) {
            // Fixate velocity to fixedArrowDamage / baseDamage for consistent damage
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
                // Apply Power enchantment bonus to perfect shot damage
                float bonusDamage = EnchantmentHelper.modifyDamage((ServerLevel) level(), getWeaponItem(), entity, damageSource, Main.CONFIG.bow.perfectShotAdditionalDamage());
                System.out.println("Perfect shot: " + bonusDamage + " bonus damage");
                // Add the bonus damage
                i.set((int) (i.get() + bonusDamage));
            }
            // Cancel vanilla crit logic since we did our own
            return false;
        }
        if (Main.CONFIG.crossbow.fixedArrowDamage() && shotFromCrossbow()) {
            // Prevent additional crossbow damage since we fixated it earlier
            return false;
        }
        return original;
    }
}
