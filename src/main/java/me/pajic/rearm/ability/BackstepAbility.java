package me.pajic.rearm.ability;

import me.pajic.rearm.effect.ReArmEffects;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BackstepAbility {
    public static final ResourceLocation BACKSTEP_EXHAUSTION = ResourceLocation.fromNamespaceAndPath("rearm", "backstep_exhaustion");

    public record C2SCauseBackstepExhaustionPayload(float exhaustion) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2SCauseBackstepExhaustionPayload> TYPE = new CustomPacketPayload.Type<>(BACKSTEP_EXHAUSTION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SCauseBackstepExhaustionPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, C2SCauseBackstepExhaustionPayload::exhaustion,
                C2SCauseBackstepExhaustionPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static boolean tryBackstep(KeyMapping backstepKey, Minecraft client) {
        if (
                backstepKey.isDown() && client.level != null && client.player != null &&
                client.player.hasEffect(ReArmEffects.BACKSTEP_EFFECT)
        ) {
            Player player = client.player;
            int backstepLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    client.level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BACKSTEP),
                    player.getMainHandItem()
            );
            if (backstepLevel > 3) backstepLevel = 3;
            Vec3 look = player.getViewVector(1);
            player.setDeltaMovement(
                    -look.x / (4 - backstepLevel),
                    player.getAttributeValue(Attributes.JUMP_STRENGTH),
                    -look.z / (4 - backstepLevel)
            );
            ClientPlayNetworking.send(new C2SCauseBackstepExhaustionPayload(5.0F));
            backstepKey.setDown(false);
            return true;
        }
        return false;
    }

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SCauseBackstepExhaustionPayload.TYPE, C2SCauseBackstepExhaustionPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
                C2SCauseBackstepExhaustionPayload.TYPE,
                (payload, context) -> context.player().causeFoodExhaustion(payload.exhaustion)
        );
    }
}
