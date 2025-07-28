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

    //? if 1.21.1 {
    @Override
    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(livingEntity, amplifier);
    }
    //?}

    //? if >= 1.21.7 {
    /*@Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        return super.applyEffectTick(level, entity, amplifier);
    }
    *///?}
}
