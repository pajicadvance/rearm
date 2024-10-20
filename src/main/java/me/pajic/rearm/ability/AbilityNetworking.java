package me.pajic.rearm.ability;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AbilityNetworking {

    public static ItemStack ACTIVE_ITEM_SERVER;
    public static Player ACTIVE_PLAYER;
    public static boolean MULTISHOT_READY_SERVER;

    public static final ResourceLocation MULTISHOT_C2S = ResourceLocation.fromNamespaceAndPath("rearm", "multishot_c2s");
    public static final ResourceLocation MULTISHOT_S2C = ResourceLocation.fromNamespaceAndPath("rearm", "multishot_s2c");
    public static final ResourceLocation ABILITY_USED_FLAG_CLEAR_S2C = ResourceLocation.fromNamespaceAndPath("rearm", "ability_used_flag_clear");

    public record C2STriggerMultishotAbilityPayload(ItemStack activeItem, UUID activePlayerUUID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2STriggerMultishotAbilityPayload> TYPE = new CustomPacketPayload.Type<>(MULTISHOT_C2S);
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

    public record S2CClearClientMultishotReadyFlagPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<S2CClearClientMultishotReadyFlagPayload> TYPE = new CustomPacketPayload.Type<>(MULTISHOT_S2C);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CClearClientMultishotReadyFlagPayload> CODEC = StreamCodec.unit(
                new S2CClearClientMultishotReadyFlagPayload()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2CClearAbilityUsedFlagPayload() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<S2CClearAbilityUsedFlagPayload> TYPE = new CustomPacketPayload.Type<>(ABILITY_USED_FLAG_CLEAR_S2C);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CClearAbilityUsedFlagPayload> CODEC = StreamCodec.unit(
                new S2CClearAbilityUsedFlagPayload()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static boolean shouldTriggerMultishot(ItemStack weapon, Entity player) {
        return MULTISHOT_READY_SERVER && ItemStack.matches(ACTIVE_ITEM_SERVER, weapon) && player.is(ACTIVE_PLAYER);
    }

    public static void initServer() {
        PayloadTypeRegistry.playC2S().register(C2STriggerMultishotAbilityPayload.TYPE, C2STriggerMultishotAbilityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CClearClientMultishotReadyFlagPayload.TYPE, S2CClearClientMultishotReadyFlagPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CClearAbilityUsedFlagPayload.TYPE, S2CClearAbilityUsedFlagPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2STriggerMultishotAbilityPayload.TYPE, (payload, context) -> {
            ACTIVE_ITEM_SERVER = payload.activeItem.copy();
            ACTIVE_PLAYER = context.player().level().getPlayerByUUID(payload.activePlayerUUID);
            MULTISHOT_READY_SERVER = true;
        });
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(S2CClearClientMultishotReadyFlagPayload.TYPE, (payload, context) ->
                MultishotAbility.MULTISHOT_READY_CLIENT = false
        );
        ClientPlayNetworking.registerGlobalReceiver(S2CClearAbilityUsedFlagPayload.TYPE, (payload, context) ->
                CooldownTracker.ABILITY_USED = true
        );
    }
}
