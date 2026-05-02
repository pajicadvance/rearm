package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.util.CompatFlags;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CripplingThrowAbility;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack itemStack, LivingEntity user);

    @WrapMethod(method = "getUseDuration")
    private int axe_useDuration(ItemStack itemStack, LivingEntity user, Operation<Integer> original) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && itemStack.is(ItemTags.AXES)) {
            return 72000;
        }
        return original.call(itemStack, user);
    }

    @WrapMethod(method = "getUseAnimation")
    private ItemUseAnimation axe_useAnimaton(
            ItemStack itemStack,
            Operation<ItemUseAnimation> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && itemStack.is(ItemTags.AXES)) {
            if (CompatFlags.HMI_LOADED) return ItemUseAnimation.NONE;
            return ItemUseAnimation.TRIDENT;
        }
        return original.call(itemStack);
    }

    @WrapMethod(method = "use")
    private InteractionResult axe_use(
            Level level, Player player, InteractionHand hand,
            Operation<InteractionResult> original
	) {
        if (ReArm.CONFIG.axe.cripplingThrow.get()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.is(ItemTags.AXES)) {
                return CripplingThrowAbility.useAxe(level, player, hand, stack);
            }
        }
        return original.call(level, player, hand);
    }

    @WrapMethod(method = "releaseUsing")
    private boolean axe_releaseUsing(
			ItemStack itemStack, Level level, LivingEntity entity, int remainingTime,
		    Operation<Boolean> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && itemStack.is(ItemTags.AXES)) {
            CripplingThrowAbility.throwAxe(itemStack, level, entity, remainingTime, getUseDuration(itemStack, entity));
        }
        return original.call(itemStack, level, entity, remainingTime);
    }
}
