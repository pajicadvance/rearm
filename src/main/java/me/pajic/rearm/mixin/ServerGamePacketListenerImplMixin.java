package me.pajic.rearm.mixin;

import me.pajic.rearm.ability.AbilityManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(
            method = "removePlayerFromWorld",
            at = @At("HEAD")
    )
    private void removePlayerAbilityData(CallbackInfo ci) {
        AbilityManager.removePlayerAbilityData(player.getUUID());
    }
}
