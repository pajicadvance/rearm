package me.pajic.rearm.mixin;

import me.pajic.rearm.projectile.ThrownAxe;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public class AxeItemMixin implements ProjectileItem {

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level level, @NotNull Position pos, @NotNull ItemStack stack, @NotNull Direction direction) {
        ThrownAxe thrownAxe = new ThrownAxe(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        thrownAxe.pickup = AbstractArrow.Pickup.ALLOWED;
        return thrownAxe;
    }
}
