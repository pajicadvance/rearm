package me.pajic.ranger.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.ranger.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "getProjectile",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z"
            )
    )
    private boolean infinityFix(boolean original, @Local(argsOnly = true) ItemStack weaponStack) {
        if (Main.CONFIG.other.infinityFix()) {
            int infinityLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.INFINITY),
                    weaponStack
            );
            return original || infinityLevel > 0;
        }
        return original;
    }
}
