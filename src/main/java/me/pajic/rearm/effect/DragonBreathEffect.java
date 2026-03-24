package me.pajic.rearm.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DragonBreathEffect extends InstantenousMobEffect {

	public DragonBreathEffect() {
		super(MobEffectCategory.HARMFUL, 0xffc600c5);
	}

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
}
