package me.pajic.rearm.mixin;

//? <26.1 {

/*import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import me.pajic.rearm.ReArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

@IfModAbsent("fabricshieldlib")
@IfModAbsent("shieldlib")
@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {

    public ShieldItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnchantmentValue() {
        return ReArm.CONFIG.shield.enchantableVanillaShield.get() ? 1 : super.getEnchantmentValue();
    }
}
*///?}
