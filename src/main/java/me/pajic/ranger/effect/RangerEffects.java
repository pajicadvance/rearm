package me.pajic.ranger.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class RangerEffects {

    public static final Holder<MobEffect> BACKSTEP_EFFECT = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            ResourceLocation.fromNamespaceAndPath("ranger", "backstep_effect"),
            new BackstepEffect()
    );

    public static void initEffects() {}
}
