package me.pajic.rearm.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public interface Platform {

	boolean isModLoaded(String modId);

	boolean isDevelopmentEnvironment();

	default boolean isDebug() {
		return isDevelopmentEnvironment();
	}

	Path getConfigDir();

	void sendToServer(CustomPacketPayload payload);

	void sendToClient(ServerPlayer player, CustomPacketPayload payload);
}
