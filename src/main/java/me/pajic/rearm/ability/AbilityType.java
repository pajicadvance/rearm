package me.pajic.rearm.ability;

import me.pajic.rearm.Main;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public enum AbilityType {
    MULTISHOT(Main.CONFIG.multishot.multishotCooldown()),
    PIERCING_SHOT(Main.CONFIG.piercingShot.piercingShotCooldown()),
    SWEEPING_EDGE(Main.CONFIG.sweepingEdge.sweepingEdgeCooldown()),
    CRIPPLING_BLOW(Main.CONFIG.cripplingBlow.cripplingBlowCooldown()),
    VOID_STRIKE(160),
    NONE(0);

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

    private final int cooldown;

    AbilityType(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }
}
