package me.pajic.rearm.mixin;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModAbsents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@IfModAbsents({@IfModAbsent("fabricshieldlib"), @IfModAbsent("shieldlib")})
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
