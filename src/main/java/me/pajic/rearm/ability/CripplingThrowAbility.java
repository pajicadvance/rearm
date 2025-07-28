package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import me.pajic.rearm.projectile.ThrownAxe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
//? if 1.21.1
import net.minecraft.world.InteractionResultHolder;
//? if >= 1.21.7
/*import net.minecraft.world.InteractionResult;*/

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CripplingThrowAbility {

    public static final Set<UUID> recallSignals = new HashSet<>();

    public static final EntityType<ThrownAxe> AXE = EntityType.Builder.<ThrownAxe>of(ThrownAxe::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .eyeHeight(0.13F)
            .updateInterval(1)
            .build(
                    //? if 1.21.1
                    "axe"
                    //? if >= 1.21.8
                    /*ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "axe"))*/
            );

    public static /*? if 1.21.1 {*/InteractionResultHolder<ItemStack>/*?}*//*? if >= 1.21.8 {*//*InteractionResult*//*?}*/ useAxe(
            Level level, Player player, InteractionHand usedHand, ItemStack stack
    ) {
        int cripplingThrowLevel = stack.getEnchantmentLevel(
                //? if 1.21.1
                level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.CRIPPLING_THROW)
                //? if >= 1.21.7
                /*level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.CRIPPLING_THROW)*/
        );
        if (cripplingThrowLevel > 0) {
            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                //? if 1.21.1
                return InteractionResultHolder.fail(stack);
                //? if >= 1.21.7
                /*return InteractionResult.FAIL;*/
            } else {
                player.startUsingItem(usedHand);
                //? if 1.21.1
                return InteractionResultHolder.consume(stack);
                //? if >= 1.21.7
                /*return InteractionResult.CONSUME;*/
            }
        }
        //? if 1.21.1
        return InteractionResultHolder.fail(stack);
        //? if >= 1.21.7
        /*return InteractionResult.FAIL;*/
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
