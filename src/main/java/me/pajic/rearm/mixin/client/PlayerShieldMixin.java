package me.pajic.rearm.mixin.client;

//? <26.1 {

/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.rearm.util.ModTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerShieldMixin extends LivingEntity {

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
        return original || useItem.is(ModTags.SHIELDS);
    }
}
*///?}
