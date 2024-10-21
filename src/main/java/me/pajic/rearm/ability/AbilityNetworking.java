package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AbilityNetworking {

    public static final ResourceLocation TRIGGER_MULTISHOT = ResourceLocation.fromNamespaceAndPath("rearm", "trigger_multishot");
    public static final ResourceLocation RESET_ABILITY_TYPE = ResourceLocation.fromNamespaceAndPath("rearm", "reset_ability_type");
    public static final ResourceLocation SIGNAL_ABILITY_USED = ResourceLocation.fromNamespaceAndPath("rearm", "signal_ability_used");

    public record C2STriggerMultishotAbilityPayload(ItemStack activeItem, UUID activePlayerUUID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2STriggerMultishotAbilityPayload> TYPE = new CustomPacketPayload.Type<>(TRIGGER_MULTISHOT);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2STriggerMultishotAbilityPayload> CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, C2STriggerMultishotAbilityPayload::activeItem,
                UUIDUtil.STREAM_CODEC, C2STriggerMultishotAbilityPayload::activePlayerUUID,
                C2STriggerMultishotAbilityPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2CResetAbilityTypePayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<S2CResetAbilityTypePayload> TYPE = new CustomPacketPayload.Type<>(RESET_ABILITY_TYPE);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CResetAbilityTypePayload> CODEC = StreamCodec.unit(
                new S2CResetAbilityTypePayload()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2CSignalAbilityUsedPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<S2CSignalAbilityUsedPayload> TYPE = new CustomPacketPayload.Type<>(SIGNAL_ABILITY_USED);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CSignalAbilityUsedPayload> CODEC = StreamCodec.unit(
                new S2CSignalAbilityUsedPayload()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private record PlayerAbilityData(ItemStack activeItem, AbilityType abilityType) {}

    private static final Object2ObjectOpenHashMap<UUID, PlayerAbilityData> playerAbilityDataMap = new Object2ObjectOpenHashMap<>();

    public static void setPlayerAbilityData(UUID playerUUID, ItemStack activeItem, AbilityType abilityType) {
        if (playerAbilityDataMap.containsKey(playerUUID)) {
            playerAbilityDataMap.replace(playerUUID, new PlayerAbilityData(activeItem, abilityType));
        }
        else {
            playerAbilityDataMap.put(playerUUID, new PlayerAbilityData(activeItem, abilityType));
        }
    }

    public static void removePlayerAbilityData(UUID playerUUID) {
        playerAbilityDataMap.remove(playerUUID);
    }

    public static ItemStack getPlayerActiveItem(UUID playerUUID) {
        if (playerAbilityDataMap.containsKey(playerUUID)) {
            return playerAbilityDataMap.get(playerUUID).activeItem;
        }
        return ItemStack.EMPTY;
    }

    public static AbilityType getPlayerAbilityType(UUID playerUUID) {
        if (playerAbilityDataMap.containsKey(playerUUID)) {
            return playerAbilityDataMap.get(playerUUID).abilityType;
        }
        return AbilityType.NONE;
    }

    public static void initServer() {
        PayloadTypeRegistry.playC2S().register(C2STriggerMultishotAbilityPayload.TYPE, C2STriggerMultishotAbilityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CResetAbilityTypePayload.TYPE, S2CResetAbilityTypePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CSignalAbilityUsedPayload.TYPE, S2CSignalAbilityUsedPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2STriggerMultishotAbilityPayload.TYPE, (payload, context) ->
                setPlayerAbilityData(payload.activePlayerUUID, payload.activeItem.copy(), AbilityType.MULTISHOT)
        );
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(S2CResetAbilityTypePayload.TYPE, (payload, context) ->
                CooldownTracker.ABILITY_TYPE = AbilityType.NONE
        );
        ClientPlayNetworking.registerGlobalReceiver(S2CSignalAbilityUsedPayload.TYPE, (payload, context) ->
                CooldownTracker.ABILITY_USED = true
        );
    }
}
