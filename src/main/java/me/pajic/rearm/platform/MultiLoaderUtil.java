package me.pajic.rearm.platform;

//$ loader_util_import
import me.pajic.rearm.platform.fabric.FabricLoaderUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public interface MultiLoaderUtil {
    MultiLoaderUtil INSTANCE = /*$ loader_util_inst*/ new FabricLoaderUtil();

    boolean isModLoaded(String modId);
    boolean isDevEnv();
    Path getConfigDir();
	void sendToServer(CustomPacketPayload payload);
	void sendToClient(ServerPlayer player, CustomPacketPayload payload);
}
