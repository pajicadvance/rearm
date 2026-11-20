package me.pajic.rearm.mixin.fix.entitysync;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.extension.ServerEntityExtension;
import net.minecraft.server.level.ServerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin implements ServerEntityExtension {

	@Shadow public abstract void sendChanges();

	@Unique private boolean rearm$pendingChanges;
	@Unique private boolean rearm$sendingDeferred;

	@WrapMethod(method = "sendChanges")
	private void entitySyncFix_deferSendChanges(Operation<Void> original) {
		if (ReArm.CONFIG.bugFixes.entitySyncFix.get() && !rearm$sendingDeferred) {
			rearm$pendingChanges = true;
			return;
		}
		original.call();
	}

	@Override
	public boolean rearm$hasPendingChanges() {
		return rearm$pendingChanges;
	}

	@Override
	public void rearm$sendDeferredChanges() {
		rearm$sendingDeferred = true;
		try {
			sendChanges();
		} finally {
			rearm$sendingDeferred = false;
			rearm$pendingChanges = false;
		}
	}
}
