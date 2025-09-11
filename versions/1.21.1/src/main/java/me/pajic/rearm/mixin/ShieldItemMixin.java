package me.pajic.rearm.mixin;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@IfModAbsent("fabricshieldlib")
@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {
    public ShieldItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnchantmentValue() {
        return 14;
    }
}
