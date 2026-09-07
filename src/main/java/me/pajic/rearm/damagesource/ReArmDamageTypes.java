package me.pajic.rearm.damagesource;

import me.pajic.rearm.ReArm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ReArmDamageTypes {

	public static final ResourceKey<DamageType> MULTISHOT_ARROW = ResourceKey.create(
			Registries.DAMAGE_TYPE,
			ReArm.id("multishot_arrow")
	);
}
