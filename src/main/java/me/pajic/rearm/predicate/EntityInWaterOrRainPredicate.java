package me.pajic.rearm.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? <26.1 {
/*import net.minecraft.advancements.critereon.EntitySubPredicate;
*///?} else >=26.1 <26.2 {
/*import net.minecraft.advancements.criterion.EntitySubPredicate;
*///?} else {
import com.mojang.serialization.Codec;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
//?}

public class EntityInWaterOrRainPredicate implements EntitySubPredicate {
	public static final EntityInWaterOrRainPredicate INSTANCE = new EntityInWaterOrRainPredicate();

	//? <26.2 {
	/*public static final MapCodec<EntityInWaterOrRainPredicate> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public @NotNull MapCodec<? extends EntitySubPredicate> codec() {
		return CODEC;
	}
	*///?} else {
	public static final Codec<EntityInWaterOrRainPredicate> CODEC = MapCodec.unit(INSTANCE).codec();
	//?}

	@Override
	public boolean matches(Entity entity, @NotNull ServerLevel level, @Nullable Vec3 position) {
		return entity.isInWaterOrRain();
	}
}
