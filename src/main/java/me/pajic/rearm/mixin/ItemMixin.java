package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.CompatFlags;
import me.pajic.rearm.Main;
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
//? if 1.21.1 {
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
//?}
//? if 1.21.7 {
/*import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemUseAnimation;
*///?}

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack stack, LivingEntity entity);

    @WrapMethod(method = "getUseDuration")
    private int axe_useDuration(ItemStack stack, LivingEntity entity, Operation<Integer> original) {
        if (Main.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            return 72000;
        }
        return original.call(stack, entity);
    }

    @WrapMethod(method = "getUseAnimation")
    private /*? if 1.21.1 {*/UseAnim/*?}*//*? if 1.21.7 {*//*ItemUseAnimation*//*?}*/ axe_useAnimaton(
            ItemStack stack,
            Operation</*? if 1.21.1 {*/UseAnim/*?}*//*? if 1.21.7 {*//*ItemUseAnimation*//*?}*/> original
    ) {
        if (Main.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            if (CompatFlags.HMI_LOADED) return /*? if 1.21.1 {*/UseAnim/*?}*//*? if 1.21.7 {*//*ItemUseAnimation*//*?}*/.NONE;
            return /*? if 1.21.1 {*/UseAnim/*?}*//*? if 1.21.7 {*//*ItemUseAnimation*//*?}*/.SPEAR;
        }
        return original.call(stack);
    }

    @WrapMethod(method = "use")
    private /*? if 1.21.1 {*/InteractionResultHolder<ItemStack>/*?}*//*? if 1.21.7 {*//*InteractionResult*//*?}*/ axe_use(
            Level level, Player player, InteractionHand usedHand,
            Operation</*? if 1.21.1 {*/InteractionResultHolder<ItemStack>/*?}*//*? if 1.21.7 {*//*InteractionResult*//*?}*/> original) {
        if (Main.CONFIG.axe.cripplingThrow.get()) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (stack.is(ItemTags.AXES)) {
                return CripplingThrowAbility.useAxe(level, player, usedHand, stack);
            }
        }
        return original.call(level, player, usedHand);
    }

    @WrapMethod(method = "releaseUsing")
    private /*? if 1.21.1 {*/void/*?}*//*? if 1.21.7 {*//*boolean*//*?}*/ axe_releaseUsing(
            ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged,
            Operation</*? if 1.21.1 {*/Void/*?}*//*? if 1.21.7 {*//*Boolean*//*?}*/> original
    ) {
        if (Main.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            CripplingThrowAbility.throwAxe(stack, level, livingEntity, timeCharged, getUseDuration(stack, livingEntity));
        }
        //? if 1.21.1 {
        else {
            original.call(stack, level, livingEntity, timeCharged);
        }
        //?}
        //? if 1.21.7
        /*return original.call(stack, level, livingEntity, timeCharged);*/
    }
}
