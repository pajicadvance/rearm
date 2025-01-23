package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.AbilityManager;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
                    target = "Lnet/minecraft/world/damagesource/DamageSources;arrow(Lnet/minecraft/world/entity/projectile/AbstractArrow;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/damagesource/DamageSource;"
            )
    )
    private DamageSource piercingShotAbility_modifyDamageSource(DamageSource original) {
        if (
                Main.CONFIG.piercingShot.piercingShotAbility() &&
                getOwner() instanceof ServerPlayer player &&
                getWeaponItem() != null &&
                AbilityManager.piercingShotAbility.shouldTriggerAbility(getWeaponItem(), player)
        ) {
            AbstractArrow arrow = (AbstractArrow) (Object) this;
            return damageSources().source(ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath("rearm", "piercing_arrow")),
                    arrow, getOwner() != null ? getOwner() : arrow
            );
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getPierceLevel()B"
            )
    )
    private byte preventPiercingIfAbilityOff(byte original) {
        if (
                Main.CONFIG.piercingShot.piercingShotAbility() &&
                getOwner() instanceof ServerPlayer player &&
                !AbilityManager.piercingShotAbility.shouldTriggerAbility(getWeaponItem(), player)
        ) {
            return 0;
        }
        return original;
    }

    @Inject(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;discard()V",
                    ordinal = 0
            )
    )
    private void resetDataOnDiscardAfterPiercing(EntityHitResult result, CallbackInfo ci) {
        if (
                Main.CONFIG.piercingShot.piercingShotAbility() &&
                getOwner() instanceof ServerPlayer player &&
                AbilityManager.piercingShotAbility.shouldTriggerAbility(getWeaponItem(), getOwner())
        ) {
            AbilityManager.resetPlayerAbilityData(player);
        }
    }

    @Inject(
            method = "onHitBlock",
            at = @At("TAIL")
    )
    private void resetDataOnHitBlock(CallbackInfo ci) {
        if (
                Main.CONFIG.piercingShot.piercingShotAbility() &&
                getOwner() instanceof ServerPlayer player &&
                AbilityManager.piercingShotAbility.shouldTriggerAbility(getWeaponItem(), getOwner())
        ) {
            AbilityManager.resetPlayerAbilityData(player);
        }
    }
}
