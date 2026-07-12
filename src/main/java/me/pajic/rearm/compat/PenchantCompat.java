package me.pajic.rearm.compat;

//? fabric {

import archives.tater.penchant.api.CanEnchantCallback;
import me.pajic.rearm.ReArm;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class PenchantCompat {

	public static void init() {
		CanEnchantCallback.STACK.register((stack, enchantment) ->
				ReArm.CONFIG.protection.allowMultipleProtectionEnchantments.get() &&
				enchantment.is(EnchantmentTags.ARMOR_EXCLUSIVE) &&
				EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet().stream().filter(
						e -> e.is(EnchantmentTags.ARMOR_EXCLUSIVE)
				).count() < ReArm.CONFIG.protection.maxProtectionEnchantments.get()
				? TriState.TRUE : TriState.DEFAULT);
	}
}
//?}
