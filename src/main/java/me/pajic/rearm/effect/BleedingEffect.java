package me.pajic.rearm.effect;

import me.pajic.rearm.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {
    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0x5c0000);
    }

    //? if 1.21.1 {
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.hurt(
                livingEntity.damageSources().magic(),
                Main.CONFIG.axe.cripplingThrowBaseBleedingDPS.get() +
                        (amplifier - 1) * Main.CONFIG.axe.cripplingThrowBleedingDPSIncreasePerLevel.get()
        );
        return true;
    }
    //?}

    //? if >= 1.21.7 {
    /*@Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        entity.hurtServer(
                level, entity.damageSources().magic(),
                Main.CONFIG.axe.cripplingThrowBaseBleedingDPS.get() +
                        (amplifier - 1) * Main.CONFIG.axe.cripplingThrowBleedingDPSIncreasePerLevel.get()
        );
        return true;
    }
    *///?}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
