package me.pajic.rearm.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
//? if > 1.21.1 {
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
//?}

public class DragonBreathEffect extends InstantenousMobEffect {

	public DragonBreathEffect() {
		super(MobEffectCategory.HARMFUL, 0xffc600c5);
	}

	//? if > 1.21.1 {
	@Override
	public boolean applyEffectTick(@NotNull ServerLevel level, LivingEntity entity, int amplifier) {
		entity.hurtServer(level, entity.damageSources().dragonBreath(), 6 << amplifier);
		return true;
	}

	@Override
	public void applyInstantenousEffect(
			@NotNull ServerLevel level,
			@Nullable Entity source,
			@Nullable Entity indirectSource,
			@NotNull LivingEntity entity,
			int amplifier,
			double health
	) {
		entity.hurtServer(level, entity.damageSources().dragonBreath(), (int) (health * (6 << amplifier) + 0.5));
	}
	//?} else {
	/*@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
		livingEntity.hurt(livingEntity.damageSources().dragonBreath(), 6 << amplifier);
		return true;
	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
		livingEntity.hurt(livingEntity.damageSources().dragonBreath(), (int) (health * (6 << amplifier) + 0.5));
	}
	*///?}
}
