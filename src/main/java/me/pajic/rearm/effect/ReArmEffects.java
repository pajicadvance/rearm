package me.pajic.rearm.effect;

import me.pajic.rearm.ReArm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class ReArmEffects {

    public static final Holder<MobEffect> BACKSTEP_EFFECT = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            ReArm.id("backstep_effect"),
            new BackstepEffect()
    );

    public static final Holder<MobEffect> BLEEDING = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            ReArm.id("bleeding"),
            new BleedingEffect()
    );

    public static void init() {}
}
