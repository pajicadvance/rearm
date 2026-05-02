package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.rearm.ReArm;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {

	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@Unique private InteractionHand hand;

	@Inject(
			method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("TAIL")
	)
	private void setHand(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		hand = owner.getUsedItemHand();
	}

	@WrapMethod(method = "tryPickup")
	private boolean tryReturnToHand(Player player, Operation<Boolean> original) {
		if (ReArm.CONFIG.trident.returnToHand.get() && pickup == Pickup.ALLOWED && !player.hasInfiniteMaterials() && hand != null && player.getItemInHand(hand).isEmpty()) {
			player.setItemInHand(hand, getPickupItem());
			return true;
		}
		return original.call(player);
	}

	@Inject(
			method = "tick",
			at = @At("HEAD")
	)
	private void returnIfInVoid(CallbackInfo ci) {
		if (ReArm.CONFIG.trident.returnFromVoid.get() && getY() < -65) setNoPhysics(true);
	}

	@Inject(
			method = "addAdditionalSaveData",
			at = @At("TAIL")
	)
	private void saveHand(ValueOutput output, CallbackInfo ci) {
		output.putBoolean("Hand", hand == InteractionHand.MAIN_HAND);
	}

	@Inject(
			method = "readAdditionalSaveData",
			at = @At("TAIL")
	)
	private void readHand(ValueInput input, CallbackInfo ci) {
		hand = input.getBooleanOr("Hand", true) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}
}
