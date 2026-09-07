package me.pajic.rearm.effect;

import net.minecraft.world.effect.InstantaneousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? >=26.1
import net.minecraft.server.level.ServerLevel;

public class DragonBreathEffect extends InstantaneousMobEffect {

	public DragonBreathEffect() {
		super(MobEffectCategory.HARMFUL, 0xffc600c5);
	}

	@Override
	public boolean applyEffectTick(/*? >=26.1 {*/@NotNull ServerLevel level, /*?}*/LivingEntity entity, int amplifier) {
        //~ if <26.1 'entity.hurtServer(level, ' -> 'entity.hurt('
        entity.hurtServer(level, entity.damageSources().dragonBreath(), 6 << amplifier);
		return true;
	}

	@Override
	public void applyInstantaneousEffect(
            /*? >=26.1 {*/@NotNull ServerLevel level,/*?}*/
			@Nullable Entity source,
			@Nullable Entity indirectSource,
			@NotNull LivingEntity entity,
			int amplifier,
			double health
	) {
        //~ if <26.1 'entity.hurtServer(level, ' -> 'entity.hurt('
        entity.hurtServer(level, entity.damageSources().dragonBreath(), (int) (health * (6 << amplifier) + 0.5));
	}
}
