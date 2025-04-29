package me.pajic.rearm.ability;

import me.pajic.rearm.enchantment.ReArmEnchantments;
import me.pajic.rearm.projectile.ThrownAxe;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CripplingThrowAbility {

    public static final Set<UUID> recallSignals = new HashSet<>();

    public static final EntityType<ThrownAxe> AXE = EntityType.Builder.<ThrownAxe>of(ThrownAxe::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .eyeHeight(0.13F)
            .updateInterval(1)
            .build("axe");

    public static InteractionResultHolder<ItemStack> useAxe(Level level, Player player, InteractionHand usedHand, ItemStack stack) {
        int cripplingThrowLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.CRIPPLING_THROW), stack
        );
        if (cripplingThrowLevel > 0) {
            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(stack);
            } else {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    public static void throwAxe(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, int useDuration) {
        if (livingEntity instanceof Player player) {
            if (useDuration - timeCharged >= 10 && !(stack.getDamageValue() >= stack.getMaxDamage() - 1) && !level.isClientSide) {
                stack.hurtAndBreak(2, player, Player.getSlotForHand(player.getUsedItemHand()));
                ThrownAxe thrownAxe = new ThrownAxe(
                        level, player, stack,
                        (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                        player.getUsedItemHand()
                );
                thrownAxe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                if (player.hasInfiniteMaterials()) thrownAxe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(thrownAxe);
                level.playSound(null, thrownAxe, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.hasInfiniteMaterials()) {
                    player.getInventory().removeItem(stack);
                }
            }
        }
    }
}
