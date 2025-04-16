package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import me.pajic.rearm.projectile.ThrownAxe;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack stack, LivingEntity entity);

    @WrapMethod(method = "getUseDuration")
    private int axe_useDuration(ItemStack stack, LivingEntity entity, Operation<Integer> original) {
        if (stack.is(ItemTags.AXES)) {
            return 72000;
        }
        return original.call(stack, entity);
    }

    @WrapMethod(method = "getUseAnimation")
    private UseAnim axe_useAnimaton(ItemStack stack, Operation<UseAnim> original) {
        if (stack.is(ItemTags.AXES)) {
            return UseAnim.SPEAR;
        }
        return original.call(stack);
    }

    @WrapMethod(method = "use")
    private InteractionResultHolder<ItemStack> axe_use(Level level, Player player, InteractionHand usedHand, Operation<InteractionResultHolder<ItemStack>> original) {
        ItemStack stack = player.getItemInHand(usedHand);
        int cripplingThrowLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.CRIPPLING_THROW), stack
        );
        if (stack.is(ItemTags.AXES) && cripplingThrowLevel > 0) {
            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(stack);
            } else {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(stack);
            }
        }
        return original.call(level, player, usedHand);
    }

    @WrapMethod(method = "releaseUsing")
    private void axe_releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, Operation<Void> original) {
        if (stack.is(ItemTags.AXES)) {
            if (livingEntity instanceof Player player) {
                int i = getUseDuration(stack, livingEntity) - timeCharged;
                if (i >= 10 && !(stack.getDamageValue() >= stack.getMaxDamage() - 1) && !level.isClientSide) {
                    stack.hurtAndBreak(2, player, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));
                    float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    ThrownAxe thrownAxe = new ThrownAxe(level, player, stack, damage);
                    thrownAxe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    if (player.hasInfiniteMaterials()) {
                        thrownAxe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    level.addFreshEntity(thrownAxe);
                    level.playSound(null, thrownAxe, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.hasInfiniteMaterials()) {
                        player.getInventory().removeItem(stack);
                    }
                }
            }
        } else {
            original.call(stack, level, livingEntity, timeCharged);
        }
    }
}
