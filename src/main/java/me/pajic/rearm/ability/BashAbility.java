package me.pajic.rearm.ability;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BashAbility {

    public static final Identifier BASH_SIGNAL = ReArm.id("bash_signal");

    public record C2SBashSignal() implements CustomPacketPayload {
        public static final Type<C2SBashSignal> TYPE = new Type<>(BASH_SIGNAL);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SBashSignal> CODEC = StreamCodec.unit(new C2SBashSignal());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

	@SuppressWarnings({"deprecation", "resource"})
	public static void handleBash(C2SBashSignal signal, ServerPlayer player) {
		ServerLevel level = player./*? if 1.21.1 {*//*serverLevel*//*?} else {*/level/*?}*/();
		int bashLevel = EnchantmentHelper.getItemEnchantmentLevel(
				//? if 1.21.1
				//level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BASH),
				//? if > 1.21.1
				level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.BASH),
				player.getUseItem()
		);
		if (bashLevel > 0 && player.isBlocking()) {
			double bashRange = ReArm.CONFIG.shield.bashBaseRange.get() + bashLevel * ReArm.CONFIG.shield.bashRangePerLevel.get();
			List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(bashRange, 1, bashRange))
					.stream().filter(livingEntity -> livingEntity != player).toList();
			if (!targets.isEmpty()) {
				targets.forEach(entity -> {
					entity.knockback(
							ReArm.CONFIG.shield.bashBaseKnockback.get() + bashLevel * ReArm.CONFIG.shield.bashKnockbackPerLevel.get(),
							Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(player.getYRot() * (float) (Math.PI / 180.0))
					);
					entity.hurt(
							level.damageSources().playerAttack(player),
							ReArm.CONFIG.shield.bashBaseDamage.get() + 2 * ReArm.CONFIG.shield.bashDamagePerLevel.get()
					);
					level.sendParticles(
							ParticleTypes.CRIT, entity.getX(), entity.getY() + 0.5, entity.getZ(),
							8, 0.3, 0.3, 0.3, 0.2
					);
				});
				level.playSound(
						null, player.getOnPos(), SoundEvents.SHIELD_BLOCK/*? if >= 1.21.7 {*/.value()/*?}*/,
						SoundSource.PLAYERS, 1.0F, 0.2F + level.random.nextFloat() * 0.3F
				);
				//? if 1.21.1 {
                            /*level.registryAccess().registryOrThrow(Registries.ITEM).getTag(ReArm.SHIELDS).ifPresent(tag ->
                                    tag.forEach(item -> player.getCooldowns().addCooldown(item.value(), ReArm.CONFIG.shield.bashShieldCooldown.get() * 20))
                            );
                            *///?} else {
				level.registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(ReArm.SHIELDS).forEach(item ->
						player.getCooldowns().addCooldown(level.registryAccess().lookupOrThrow(Registries.ITEM).getKey(item.value()), ReArm.CONFIG.shield.bashShieldCooldown.get() * 20)
				);
				//?}
				player.getUseItem().hurtAndBreak(
						targets.size(), player,
						player.getMainHandItem().is(player.getUseItem().getItem()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND
				);
				player.stopUsingItem();
			}
		}
	}
}
