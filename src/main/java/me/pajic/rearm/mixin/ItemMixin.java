package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.ability.CripplingThrowAbility;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//~ if <26.1 'ItemUseAnimation' -> 'UseAnim'
import net.minecraft.world.item.ItemUseAnimation;

//~ if <26.1 'InteractionResult' -> 'InteractionResultHolder'
import net.minecraft.world.InteractionResult;

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
    //~ if <26.1 'ItemUseAnimation' -> 'UseAnim' {
    private ItemUseAnimation axe_useAnimation(
            ItemStack itemStack,
            Operation<ItemUseAnimation> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && itemStack.is(ItemTags.AXES)) {
            //~ if <26.1 'TRIDENT' -> 'SPEAR'
            return ItemUseAnimation.TRIDENT;
        }
        return original.call(itemStack);
    }
    //~}

    //~ if <26.1 'InteractionResult' -> 'InteractionResultHolder<ItemStack>' {
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
    //~}

    @WrapMethod(method = "releaseUsing")
    //~ if <26.1 'boolean' -> 'void'
    private boolean axe_releaseUsing(
            //~ if <26.1 'Boolean' -> 'Void'
			ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, Operation<Boolean> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && itemStack.is(ItemTags.AXES)) {
            /*? >=26.1 {*/return /*?}*/CripplingThrowAbility.throwAxe(itemStack, level, entity, remainingTime, getUseDuration(itemStack, entity));
        }
        //~ if <26.1 'return' -> 'else'
        return original.call(itemStack, level, entity, remainingTime);
    }
}
