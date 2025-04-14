package me.pajic.rearm.ability;

import com.mojang.serialization.Codec;
import me.pajic.rearm.projectile.ThrownAxe;
import me.pajic.rearm.projectile.ThrownAxeRenderer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class CripplingThrowAbility {

    public static final ResourceLocation RECALL_AXE = ResourceLocation.fromNamespaceAndPath("rearm", "recall_axe");

    public record C2SUpdatePlayerRecallCondition(UUID activePlayerUUID) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2SUpdatePlayerRecallCondition> TYPE = new CustomPacketPayload.Type<>(RECALL_AXE);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdatePlayerRecallCondition> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SUpdatePlayerRecallCondition::activePlayerUUID,
                C2SUpdatePlayerRecallCondition::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static final EntityType<ThrownAxe> AXE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.parse("rearm:axe"),
            EntityType.Builder.<ThrownAxe>of(ThrownAxe::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build()
    );
    public static final AttachmentType<Long> THROWN_AXE_TICKS_ACTIVE = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath("rearm", "thrown_axe_ticks_active"),
            builder -> builder
                    .initializer(() -> 0L)
                    .persistent(Codec.LONG)
                    .syncWith(ByteBufCodecs.VAR_LONG, AttachmentSyncPredicate.all())
    );
    public static final AttachmentType<ItemStack> THROWN_AXE_ITEM_STACK = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath("rearm", "thrown_axe_item_stack"),
            builder -> builder
                    .initializer(() -> ItemStack.EMPTY)
                    .persistent(ItemStack.OPTIONAL_CODEC)
                    .syncWith(ItemStack.OPTIONAL_STREAM_CODEC, AttachmentSyncPredicate.all())
    );

    public static final Set<UUID> recallSignals = new HashSet<>();

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SUpdatePlayerRecallCondition.TYPE, C2SUpdatePlayerRecallCondition.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(C2SUpdatePlayerRecallCondition.TYPE, (payload, context) ->
                recallSignals.add(payload.activePlayerUUID)
        );
    }

    public static void initClient() {
        EntityRendererRegistry.register(CripplingThrowAbility.AXE, ThrownAxeRenderer::new);
    }
}
