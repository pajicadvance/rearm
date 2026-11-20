package me.pajic.rearm.mixin.fix.entitysync;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.extension.ChunkMapExtension;
import me.pajic.rearm.extension.ServerEntityExtension;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements ChunkMapExtension {

	@Shadow @Final private Int2ObjectMap<?> entityMap;

	@WrapWithCondition(
			method = "tick()V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerEntity;sendChanges()V"
			)
	)
	private boolean entitySyncFix_deferSendChanges(ServerEntity serverEntity, @Local(ordinal = 0) boolean sectionPosChanged) {
		if (ReArm.CONFIG.bugFixes.entitySyncFix.get()) {
			if (sectionPosChanged) ((ServerEntityExtension) serverEntity).rearm$setSectionPosChanged(true);
			return false;
		}
		return true;
	}

	@Override
	public void rearm$sendEntityChanges() {
		for (Object object : entityMap.values()) {
			TrackedEntityAccessor trackedEntity = (TrackedEntityAccessor) object;
			ServerEntityExtension serverEntity = (ServerEntityExtension) trackedEntity.rearm$getServerEntity();
            ChunkMap self = (ChunkMap) (Object) this;
			if (
					serverEntity.rearm$isSectionPosChanged() ||
					//? >=26.1
					trackedEntity.rearm$getEntity().needsSync ||
					//~ if <26.1 'pack()' -> 'toLong()'
					self.getDistanceManager().inEntityTickingRange(trackedEntity.rearm$getLastSectionPos().chunk().pack())
			) {
				trackedEntity.rearm$getServerEntity().sendChanges();
				serverEntity.rearm$setSectionPosChanged(false);
			}
		}
	}
}
