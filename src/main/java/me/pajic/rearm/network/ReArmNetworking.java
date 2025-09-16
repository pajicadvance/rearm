package me.pajic.rearm.network;

import me.pajic.rearm.Main;
import me.pajic.rearm.ability.CooldownTracker;
import me.pajic.rearm.ability.CripplingThrowAbility;
import me.pajic.rearm.ability.CriticalCounterAbility;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
//? if >= 1.21.8
/*import net.neoforged.neoforge.client.network.ClientPacketDistributor;*/

import java.util.List;
import java.util.UUID;

public class ReArmNetworking {

    public static final ResourceLocation BACKSTEP_EXHAUSTION = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "backstep_exhaustion");
    public static final ResourceLocation COUNTER_START_TIMER = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "counter_start_timer");
    public static final ResourceLocation UPDATE_PLAYER_COUNTER_CONDITION = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "update_player_counter_condition");
    public static final ResourceLocation RECALL_AXE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "recall_axe");
    public static final ResourceLocation BASH_SIGNAL = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "bash_signal");

    public record C2SCauseBackstepExhaustionPayload(float exhaustion) implements CustomPacketPayload {
        public static final Type<C2SCauseBackstepExhaustionPayload> TYPE = new Type<>(BACKSTEP_EXHAUSTION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SCauseBackstepExhaustionPayload> CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, C2SCauseBackstepExhaustionPayload::exhaustion,
                C2SCauseBackstepExhaustionPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SUpdatePlayerCounterCondition(UUID activePlayerUUID, boolean shouldCounter) implements CustomPacketPayload {
        public static final Type<C2SUpdatePlayerCounterCondition> TYPE = new Type<>(UPDATE_PLAYER_COUNTER_CONDITION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdatePlayerCounterCondition> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SUpdatePlayerCounterCondition::activePlayerUUID,
                ByteBufCodecs.BOOL, C2SUpdatePlayerCounterCondition::shouldCounter,
                C2SUpdatePlayerCounterCondition::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2CStartCriticalCounterTimer() implements CustomPacketPayload {
        public static final Type<S2CStartCriticalCounterTimer> TYPE = new Type<>(COUNTER_START_TIMER);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CStartCriticalCounterTimer> CODEC = StreamCodec.unit(
                new S2CStartCriticalCounterTimer()
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SUpdatePlayerRecallCondition(UUID activePlayerUUID) implements CustomPacketPayload {
        public static final Type<C2SUpdatePlayerRecallCondition> TYPE = new Type<>(RECALL_AXE);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SUpdatePlayerRecallCondition> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SUpdatePlayerRecallCondition::activePlayerUUID,
                C2SUpdatePlayerRecallCondition::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SBashSignal() implements CustomPacketPayload {
        public static final Type<C2SBashSignal> TYPE = new Type<>(BASH_SIGNAL);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SBashSignal> CODEC = StreamCodec.unit(new C2SBashSignal());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void init(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                C2SCauseBackstepExhaustionPayload.TYPE,
                C2SCauseBackstepExhaustionPayload.CODEC,
                (payload, context) -> context.player().causeFoodExhaustion(payload.exhaustion)
        );
        registrar.playToServer(
                C2SUpdatePlayerCounterCondition.TYPE,
                C2SUpdatePlayerCounterCondition.CODEC,
                (payload, context) ->
                        CriticalCounterAbility.setPlayerCounterCondition(payload.activePlayerUUID(), payload.shouldCounter())
        );
        registrar.playToServer(
                C2SUpdatePlayerRecallCondition.TYPE,
                C2SUpdatePlayerRecallCondition.CODEC,
                (payload, context) ->
                        CripplingThrowAbility.recallSignals.add(payload.activePlayerUUID)
        );
        registrar.playToServer(
                C2SBashSignal.TYPE,
                C2SBashSignal.CODEC,
                (payload, context) -> {
                    ServerPlayer player = (ServerPlayer) context.player();
                    ServerLevel level = player./*? if 1.21.1 {*/serverLevel/*?}*//*? if >= 1.21.7 {*//*level*//*?}*/();
                    int bashLevel = player.getUseItem().getEnchantmentLevel(
                            //? if 1.21.1
                            level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BASH)
                            //? if >= 1.21.7
                            /*level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.BASH)*/
                    );
                    if (bashLevel > 0 && player.isBlocking()) {
                        double bashRange = Main.CONFIG.shield.bashBaseRange.get() + bashLevel * Main.CONFIG.shield.bashRangePerLevel.get();
                        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(bashRange, 1, bashRange));
                        targets.forEach(entity -> {
                            if (entity != player) {
                                entity.knockback(
                                        Main.CONFIG.shield.bashBaseKnockback.get() + bashLevel * Main.CONFIG.shield.bashKnockbackPerLevel.get(),
                                        Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(player.getYRot() * (float) (Math.PI / 180.0))
                                );
                                entity.hurt(
                                        level.damageSources().playerAttack(player),
                                        Main.CONFIG.shield.bashBaseDamage.get() + 2 * Main.CONFIG.shield.bashDamagePerLevel.get()
                                );
                                level.sendParticles(
                                        ParticleTypes.CRIT, entity.getX(), entity.getY() + 0.5, entity.getZ(),
                                        8, 0.3, 0.3, 0.3, 0.2
                                );
                            }
                        });
                        level.playSound(
                                null, player.getOnPos(), SoundEvents.SHIELD_BLOCK/*? if >= 1.21.7 {*//*.value()*//*?}*/,
                                SoundSource.PLAYERS, 1.0F, 0.2F + level.random.nextFloat() * 0.3F
                        );
                        //? if < 1.21.8 {
                        level.registryAccess().registryOrThrow(Registries.ITEM).getTag(Main.SHIELDS).ifPresent(tag ->
                                tag.forEach(item -> player.getCooldowns().addCooldown(item.value(), Main.CONFIG.shield.bashShieldCooldown.get() * 20))
                        );
                        //?}
                        //? if >= 1.21.8 {
                        /*level.registryAccess().lookupOrThrow(Registries.ITEM).getTagOrEmpty(Main.SHIELDS).forEach(item ->
                                player.getCooldowns().addCooldown(level.registryAccess().lookupOrThrow(Registries.ITEM).getKey(item.value()), Main.CONFIG.shield.bashShieldCooldown.get() * 20)
                        );
                        *///?}
                        player.getUseItem().hurtAndBreak(
                                targets.size(), player,
                                player.getMainHandItem().is(player.getUseItem().getItem()) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND
                        );
                        player.stopUsingItem();
                    }
                }
        );
        registrar.playToClient(
                S2CStartCriticalCounterTimer.TYPE,
                S2CStartCriticalCounterTimer.CODEC,
                (payload, context) -> CooldownTracker.counterTimerActive = true
        );
    }

    public static void sendToServer(CustomPacketPayload payload) {
        //? if 1.21.1
        PacketDistributor.sendToServer(payload);
        //? if >= 1.21.8
        /*ClientPacketDistributor.sendToServer(payload);*/
    }
}
