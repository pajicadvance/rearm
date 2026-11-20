package me.pajic.rearm.mixin.fix.entitysync;

import me.pajic.rearm.extension.ServerEntityExtension;
import net.minecraft.server.level.ServerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerEntity.class)
public class ServerEntityMixin implements ServerEntityExtension {

	@Unique private boolean rearm$sectionPosChanged;

	@Override
	public boolean rearm$isSectionPosChanged() {
		return rearm$sectionPosChanged;
	}

	@Override
	public void rearm$setSectionPosChanged(boolean sectionPosChanged) {
		rearm$sectionPosChanged = sectionPosChanged;
	}
}
