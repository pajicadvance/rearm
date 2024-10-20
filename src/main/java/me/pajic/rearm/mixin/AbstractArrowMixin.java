package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
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
    @Shadow private @Nullable ItemStack firedFromWeapon;

    @ModifyExpressionValue(
            method = "shotFromCrossbow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean ranger_shotFromCrossbow(boolean original) {
        return original || firedFromWeapon.is(ReArmItems.NETHERITE_CROSSBOW);
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;length()D"
            )
    )
    private double modifyVelocity(double original) {
        System.out.println(original);
        if (Main.CONFIG.bow.enablePerfectShot() && !shotFromCrossbow() && original > 3.0 && original < 3.5) {
            // Cap bow velocity to 3.0 for consistent damage.
            // If velocity is 3.5 or higher, we assume that the player is doing a trick shot,
            // and allow the damage increase.
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
