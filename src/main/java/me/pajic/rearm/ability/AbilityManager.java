package me.pajic.rearm.ability;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AbilityManager {

    public static final MultishotAbility multishotAbility = new MultishotAbility();
    public static final PiercingShotAbility piercingShotAbility = new PiercingShotAbility();
    public static final SweepingEdgeAbility sweepingEdgeAbility = new SweepingEdgeAbility();
    public static final ImmutableList<Ability> abilities = ImmutableList.of(multishotAbility, piercingShotAbility, sweepingEdgeAbility);

    public static final ResourceLocation TRIGGER_ABILITY = ResourceLocation.fromNamespaceAndPath("rearm", "trigger_ability");
    public static final ResourceLocation RESET_ABILITY_TYPE = ResourceLocation.fromNamespaceAndPath("rearm", "reset_ability_type");
    public static final ResourceLocation SIGNAL_ABILITY_USED = ResourceLocation.fromNamespaceAndPath("rearm", "signal_ability_used");

    public record C2STriggerAbilityPayload(ItemStack activeItem, AbilityType abilityType, UUID activePlayerUUID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2STriggerAbilityPayload> TYPE = new CustomPacketPayload.Type<>(TRIGGER_ABILITY);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2STriggerAbilityPayload> CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC, C2STriggerAbilityPayload::activeItem,
                AbilityType.STREAM_CODEC, C2STriggerAbilityPayload::abilityType,
                UUIDUtil.STREAM_CODEC, C2STriggerAbilityPayload::activePlayerUUID,
                C2STriggerAbilityPayload::new
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

    public static void resetPlayerAbilityData(ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, new S2CResetAbilityTypePayload());
        setPlayerAbilityData(serverPlayer.getUUID(), ItemStack.EMPTY, AbilityType.NONE);
    }

    public static void setPlayerAbilityUsed(ServerPlayer player) {
        ServerPlayNetworking.send(player, new AbilityManager.S2CSignalAbilityUsedPayload());
    }

    public static boolean tryAbilities(KeyMapping abilityKey, Minecraft client) {
        for (Ability ability : abilities) {
            if (ability.tryAbility(abilityKey, client)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldRenderHotbarActiveIndicator(ItemStack stack, LocalPlayer player) {
        for (Ability ability : abilities) {
            if (ability.shouldRenderHotbarActiveIndicator(stack, player)) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldRenderHotbarCooldownIndicator(ItemStack stack, Minecraft client) {
        for (Ability ability : abilities) {
            if (ability.shouldRenderHotbarCooldownIndicator(stack, client)) {
                return true;
            }
        }
        return false;
    }

    public static void initServer() {
        PayloadTypeRegistry.playC2S().register(C2STriggerAbilityPayload.TYPE, C2STriggerAbilityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CResetAbilityTypePayload.TYPE, S2CResetAbilityTypePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CSignalAbilityUsedPayload.TYPE, S2CSignalAbilityUsedPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2STriggerAbilityPayload.TYPE, (payload, context) ->
                setPlayerAbilityData(payload.activePlayerUUID, payload.activeItem.copy(), payload.abilityType)
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
