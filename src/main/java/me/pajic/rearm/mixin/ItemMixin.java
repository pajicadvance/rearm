package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.Main;
import me.pajic.rearm.ability.CripplingThrowAbility;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
    private UseAnim axe_useAnimaton(ItemStack stack, Operation<UseAnim> original) {
        if (Main.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            if (FabricLoader.getInstance().isModLoaded("hold-my-items")) return UseAnim.NONE;
            return UseAnim.SPEAR;
        }
        return original.call(stack);
    }

    @WrapMethod(method = "use")
    private InteractionResultHolder<ItemStack> axe_use(Level level, Player player, InteractionHand usedHand, Operation<InteractionResultHolder<ItemStack>> original) {
        if (Main.CONFIG.axe.cripplingThrow.get()) {
            ItemStack stack = player.getItemInHand(usedHand);
            if (stack.is(ItemTags.AXES)) {
                return CripplingThrowAbility.useAxe(level, player, usedHand, stack);
            }
        }
        return original.call(level, player, usedHand);
    }

    @WrapMethod(method = "releaseUsing")
    private void axe_releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, Operation<Void> original) {
        if (Main.CONFIG.axe.cripplingThrow.get() && stack.is(ItemTags.AXES)) {
            CripplingThrowAbility.throwAxe(stack, level, livingEntity, timeCharged, getUseDuration(stack, livingEntity));
        } else {
            original.call(stack, level, livingEntity, timeCharged);
        }
    }
}
