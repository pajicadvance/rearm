package me.pajic.rearm.mixin.misc;

import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.ability.QuickstepAbility;
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
    private void removePlayerData(CallbackInfo ci) {
        CriticalCounterAbility.removePlayerCounterConditionData(player.getUUID());
        CripplingThrowAbility.recallSignals.remove(player.getUUID());
        QuickstepAbility.removePlayerData(player);
    }
}
