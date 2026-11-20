package me.pajic.rearm.mixin.fix.entitysync;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.extension.ChunkMapExtension;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

	@Inject(
			method = "tick(Ljava/util/function/BooleanSupplier;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;tick()V",
					shift = At.Shift.AFTER
			)
	)
	private void entitySyncFix_sendEntityChanges(CallbackInfo ci) {
		if (ReArm.CONFIG.bugFixes.entitySyncFix.get()) {
			((ChunkMapExtension) ((ServerLevel) (Object) this).getChunkSource().chunkMap).rearm$sendEntityChanges();
		}
	}
}
