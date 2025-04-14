package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"
            ),
            index = 3
    )
    private float pierceArmor(float original, @Local(argsOnly = true) DamageSource source) {
        if (Main.CONFIG.crossbow.improvedPiercing()) {
            int piercingLevel = source.getWeaponItem() != null ?
                    EnchantmentHelper.getItemEnchantmentLevel(
                            registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PIERCING),
                            source.getWeaponItem()
                    ) : 0;
            return original * (1 - ((float) (Main.CONFIG.crossbow.percentArmorIgnoredPerLevel() * piercingLevel) / 100));
        }
        return original;
    }
}
