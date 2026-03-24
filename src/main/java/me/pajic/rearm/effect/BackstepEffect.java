package me.pajic.rearm.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BackstepEffect extends MobEffect {

    public BackstepEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x94fffb);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        return super.applyEffectTick(level, entity, amplifier);
    }
}
