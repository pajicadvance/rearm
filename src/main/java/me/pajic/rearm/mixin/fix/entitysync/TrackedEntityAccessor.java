package me.pajic.rearm.mixin.fix.entitysync;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity")
public interface TrackedEntityAccessor {

	@Accessor("serverEntity") ServerEntity rearm$getServerEntity();
	@Accessor("entity") Entity rearm$getEntity();
	@Accessor("lastSectionPos") SectionPos rearm$getLastSectionPos();
}
