package me.pajic.rearm.platform.neoforge;

//? neoforge {

/*import me.pajic.rearm.platform.MultiLoaderUtil;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;

//? >=26.1
//import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoforgeLoaderUtil implements MultiLoaderUtil {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevEnv() {
        return !FMLLoader/^? >=1.21.9 {^//^.getCurrent()^//^?}^/.isProduction();
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        //~ if <26.1 'ClientPacketDistributor' -> 'PacketDistributor'
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
*///?}
