package me.pajic.rearm.effect;

import me.pajic.rearm.Main;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {
    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0x5c0000);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.hurt(
                livingEntity.damageSources().magic(),
                Main.CONFIG.axe.cripplingThrowBaseBleedingDPS() +
                        (amplifier - 1) * Main.CONFIG.axe.cripplingThrowBleedingDPSIncreasePerLevel()
        );
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
