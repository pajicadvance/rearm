package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.Main;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {

    @ModifyExpressionValue(
            method = "dealExplosionDamage",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/List;isEmpty()Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;position()Lnet/minecraft/world/phys/Vec3;"
                    )
            ),
            at = @At(value = "CONSTANT", args = "floatValue=5.0")
    )
    private float modifyBaseFireworkDamage(float original) {
        if (Main.CONFIG.crossbow.modifyFireworkDamage()) {
            return Main.CONFIG.crossbow.baseFireworkDamage();
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "dealExplosionDamage",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Ljava/util/List;isEmpty()Z"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;position()Lnet/minecraft/world/phys/Vec3;"
                    )
            ),
            at = @At(value = "CONSTANT", args = "intValue=2")
    )
    private int modifyFireworkStarAdditionalDamage(int original) {
        if (Main.CONFIG.crossbow.modifyFireworkDamage()) {
            return Main.CONFIG.crossbow.damagePerFireworkStar();
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "dealExplosionDamage",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"
                    )
            ),
            at = @At(value = "CONSTANT", args = "doubleValue=5.0")
    )
    private double modifyResultingFireworkDamage(double original) {
        if (Main.CONFIG.crossbow.modifyFireworkDamage()) {
            return Main.CONFIG.crossbow.baseFireworkDamage();
        }
        return original;
    }
}
