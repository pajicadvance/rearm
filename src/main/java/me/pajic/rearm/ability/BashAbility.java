package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import me.pajic.rearm.enchantment.ReArmEnchantments;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

public class BashAbility {

    public static final ResourceLocation BASH_SIGNAL = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "bash_signal");

    public record C2SBashSignal() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<C2SBashSignal> TYPE = new CustomPacketPayload.Type<>(BASH_SIGNAL);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SBashSignal> CODEC = StreamCodec.unit(new C2SBashSignal());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SBashSignal.TYPE, C2SBashSignal.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                C2SBashSignal.TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();
                    ServerLevel level = player./*? if 1.21.1 {*/serverLevel/*?}*//*? if >= 1.21.7 {*//*level*//*?}*/();
                    int bashLevel = EnchantmentHelper.getItemEnchantmentLevel(
                            //? if 1.21.1
                            level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(ReArmEnchantments.BASH),
                            //? if >= 1.21.7
                            /*level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ReArmEnchantments.BASH),*/
                            player.getUseItem()
                    );
                    if (bashLevel > 0 && player.isBlocking()) {
                        double bashRange = 1.5 + bashLevel * 0.5;
                        level.getEntitiesOfClass(
                                LivingEntity.class, player.getBoundingBox().inflate(bashRange, 1, bashRange)
                        ).forEach(entity -> {
                            if (entity != player) {
                                entity.knockback(0.75 + bashLevel * 0.75, Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)));
                                entity.hurt(level.damageSources().playerAttack(player), 2 * bashLevel);
                                level.sendParticles(ParticleTypes.CRIT, entity.getX(), entity.getY() + 0.5, entity.getZ(), 8, 0.3, 0.3, 0.3, 0.2);
                            }
                        });
                        level.playSound(null, player.getOnPos(), SoundEvents.SHIELD_BLOCK/*? if >= 1.21.7 {*//*.value()*//*?}*/, SoundSource.PLAYERS, 1.0F, 0.2F + level.random.nextFloat() * 0.3F);
                        player.getCooldowns().addCooldown(/*? if 1.21.1 {*/Items.SHIELD/*?}*//*? if >= 1.21.7 {*//*player.getUseItem()*//*?}*/, 160);
                        player.stopUsingItem();
                    }
                }
        );
    }
}
