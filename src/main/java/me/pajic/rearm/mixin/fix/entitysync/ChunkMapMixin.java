package me.pajic.rearm.mixin.fix.entitysync;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.pajic.rearm.extension.ChunkMapExtension;
import me.pajic.rearm.extension.ServerEntityExtension;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements ChunkMapExtension {

	@Shadow @Final private Int2ObjectMap<?> entityMap;

	@Override
	public void rearm$sendEntityChanges() {
		ChunkMap self = (ChunkMap) (Object) this;
		for (Object object : entityMap.values()) {
			TrackedEntityAccessor trackedEntity = (TrackedEntityAccessor) object;
			ServerEntityExtension serverEntity = (ServerEntityExtension) trackedEntity.rearm$getServerEntity();
			if (
					serverEntity.rearm$hasPendingChanges() ||
					//? >=26.1
					trackedEntity.rearm$getEntity().needsSync ||
					//~ if <26.1 'pack()' -> 'toLong()'
					self.getDistanceManager().inEntityTickingRange(trackedEntity.rearm$getLastSectionPos().chunk().pack())
			) {
				serverEntity.rearm$sendDeferredChanges();
			}
		}
	}
}
