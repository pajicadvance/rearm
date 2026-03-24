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

    @Shadow public abstract int getUseDuration(ItemStack stack, LivingEntity entity);

    @WrapMethod(method = "getUseDuration")
    private int axe_useDuration(ItemStack stack, LivingEntity entity, Operation<Integer> original) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            return 72000;
        }
        return original.call(stack, entity);
    }

    @WrapMethod(method = "getUseAnimation")
    private ItemUseAnimation axe_useAnimaton(
            ItemStack stack,
            Operation<ItemUseAnimation> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            if (CompatFlags.HMI_LOADED) return ItemUseAnimation.NONE;
            return ItemUseAnimation.TRIDENT;
        }
        return original.call(stack);
    }

    @WrapMethod(method = "use")
    private InteractionResult axe_use(
            Level level, Player player, InteractionHand usedHand,
            Operation<InteractionResult> original) {
        if (ReArm.CONFIG.axe.cripplingThrow.get()) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (stack.is(ItemTags.AXES)) {
                return CripplingThrowAbility.useAxe(level, player, usedHand, stack);
            }
        }
        return original.call(level, player, usedHand);
    }

    @WrapMethod(method = "releaseUsing")
    private boolean axe_releaseUsing(
            ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged,
            Operation<Boolean> original
    ) {
        if (ReArm.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            CripplingThrowAbility.throwAxe(stack, level, livingEntity, timeCharged, getUseDuration(stack, livingEntity));
        }
        return original.call(stack, level, livingEntity, timeCharged);
    }
}
