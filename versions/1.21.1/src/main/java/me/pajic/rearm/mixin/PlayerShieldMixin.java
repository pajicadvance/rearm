package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerShieldMixin extends LivingEntity {
    @Shadow
    public abstract ItemCooldowns getCooldowns();

    protected PlayerShieldMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "hurtCurrentlyUsedShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/neoforged/neoforge/common/ItemAbility;)Z"
            )
    )
    private boolean extendShieldCheck(boolean original) {
        return original || useItem.is(Main.SHIELDS);
    }

    @Inject(
            method = "disableShield",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V"
            )
    )
    private void disableAllShields(CallbackInfo ci) {
        //? if < 1.21.8 {
        level().registryAccess().registryOrThrow(Registries.ITEM).getTag(Main.SHIELDS).ifPresent(tag ->
                tag.forEach(item -> getCooldowns().addCooldown(item.value(), 160))
        );
        //?}
        //? if >= 1.21.8 {
        /*level().registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(Main.SHIELDS).forEach(item ->
                getCooldowns().addCooldown(level.registryAccess().lookupOrThrow(Registries.ITEM).getKey(item.value()), 160)
        );
        *///?}
    }
}
