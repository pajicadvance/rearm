package me.pajic.rearm.ability;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public enum AbilityType {
    MULTISHOT,
    PIERCING_SHOT,
    SWEEPING_EDGE,
    CRIPPLING_BLOW,
    VOID_STRIKE,
    NONE;

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityType> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, @NotNull AbilityType abilityType) {
            buf.writeEnum(abilityType);
        }

        @Override
        public @NotNull AbilityType decode(@NotNull RegistryFriendlyByteBuf buf) {
            return buf.readEnum(AbilityType.class);
        }
    };
}
