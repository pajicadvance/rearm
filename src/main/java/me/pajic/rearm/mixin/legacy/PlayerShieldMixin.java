package me.pajic.rearm.mixin.legacy;

//? if 1.21.1 {

/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.ReArm;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

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
					//? if fabric
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
					//? if neoforge
					//target = "Lnet/minecraft/world/item/ItemStack;canPerformAction(Lnet/neoforged/neoforge/common/ItemAbility;)Z"
            )
    )
    private boolean extendShieldCheck(boolean original) {
        return original || useItem.is(ReArm.SHIELDS);
    }
}
*///?}
