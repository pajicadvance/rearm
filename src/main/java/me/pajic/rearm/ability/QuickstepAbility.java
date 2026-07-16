package me.pajic.rearm.ability;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.pajic.rearm.ReArm;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class QuickstepAbility {

    public static final Identifier BACKSTEP_EXHAUSTION = ReArm.id("backstep_exhaustion");
	private static final Identifier BACKSTEP_HEIGHT_MODIFIER = ReArm.id("backstep_height");

	public static final Object2IntOpenHashMap<UUID> playerStepHeightTracker = new Object2IntOpenHashMap<>();

    public record C2SQuickstepSignal() implements CustomPacketPayload {
        public static final Type<C2SQuickstepSignal> TYPE = new Type<>(BACKSTEP_EXHAUSTION);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SQuickstepSignal> CODEC = StreamCodec.unit(new C2SQuickstepSignal());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

	public static void handleQuickstep(ServerPlayer player) {
		AttributeInstance stepHeight = player.getAttribute(Attributes.STEP_HEIGHT);
		if (stepHeight != null) {
			double currentValue = stepHeight.getValue();
			if (currentValue < 1) {
				stepHeight.addOrReplacePermanentModifier(new AttributeModifier(
						BACKSTEP_HEIGHT_MODIFIER,
						1 - currentValue,
						AttributeModifier.Operation.ADD_VALUE
				));
				playerStepHeightTracker.put(player.getUUID(), 20);
			}
		}
		dash(player);
		player.causeFoodExhaustion(1);
	}

	public static void dash(Player player) {
		Vec3 motion = player.getDeltaMovement();
		Vec3 horizontalMotion = new Vec3(motion.x, 0, motion.z);
		if (horizontalMotion.lengthSqr() > 1.0E-4) {
			player.addDeltaMovement(horizontalMotion.normalize().scale(4d/3));
		}
	}

	public static void onServerTick(MinecraftServer server) {
		if (!playerStepHeightTracker.isEmpty()) {
			playerStepHeightTracker.replaceAll((_, i) -> i - 1);
			playerStepHeightTracker.keySet().removeIf(uuid -> {
				boolean shouldRemove = playerStepHeightTracker.getInt(uuid) <= 0;
				if (shouldRemove) {
					ServerPlayer player = server.getPlayerList().getPlayer(uuid);
					if (player != null) {
						AttributeInstance stepHeight = player.getAttribute(Attributes.STEP_HEIGHT);
						if (stepHeight != null && stepHeight.hasModifier(BACKSTEP_HEIGHT_MODIFIER)) {
							stepHeight.removeModifier(BACKSTEP_HEIGHT_MODIFIER);
						}
					}
				}
				return shouldRemove;
			});
		}
	}
}
