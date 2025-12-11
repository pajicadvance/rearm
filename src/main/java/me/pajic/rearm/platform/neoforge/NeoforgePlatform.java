package me.pajic.rearm.platform.neoforge;

//? neoforge {

/*import me.pajic.rearm.platform.Platform;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import java.nio.file.Path;
import net.neoforged.neoforge.network.PacketDistributor;
//? if > 1.21.1
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoforgePlatform implements Platform {

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDebug() {
		return !FMLLoader/^? if > 1.21.1 {^/.getCurrent()/^?}^/.isProduction();
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public ModLoader loader() {
		return ModLoader.NEOFORGE;
	}

	@Override
	public String mcVersion() {
		return FMLLoader/^? if 1.21.1 {^//^.versionInfo()^//^?} else {^/.getCurrent().getVersionInfo()/^?}^/.mcVersion();
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		//? if 1.21.1
		//PacketDistributor.sendToServer(payload);
		//? if > 1.21.1
		ClientPacketDistributor.sendToServer(payload);
	}

	@Override
	public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}
}
*///?}
