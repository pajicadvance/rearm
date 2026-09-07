package me.pajic.rearm.effect;

import me.pajic.rearm.ReArm;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

//? >=26.1 {
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
//?}

public class BleedingEffect extends MobEffect {

    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0x5c0000);
    }

    @Override
    public boolean applyEffectTick(/*? >=26.1 {*/@NotNull ServerLevel level, /*?}*/LivingEntity entity, int amplifier) {
        //~ if <26.1 'entity.hurtServer(level,' -> 'entity.hurt('
        entity.hurtServer(level,
                entity.damageSources().magic(),
				ReArm.CONFIG.axe.cripplingThrowBaseBleedingDPS.get() +
                        (amplifier - 1) * ReArm.CONFIG.axe.cripplingThrowBleedingDPSIncreasePerLevel.get()
        );
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
