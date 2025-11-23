package me.pajic.rearm.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public interface Platform {
	boolean isModLoaded(String modId);
	boolean isDebug();
	Path getConfigDir();
	ModLoader loader();
	String mcVersion();

	void sendToServer(CustomPacketPayload payload);
	void sendToClient(ServerPlayer player, CustomPacketPayload payload);

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE
	}
}
