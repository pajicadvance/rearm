package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.util.Index;

import java.util.List;

public class AssetPatches {

	public static void init() {
		// Enchantment tooltip order
		MixsonHelper.registerSingleJsonPersistent(
				"Adjust enchantment tooltip order",
				new Index("minecraft:tags/enchantment/tooltip_order"),
				context -> {
					List<JsonElement> values = context.getFile().getAsJsonObject().getAsJsonArray("values").asList();
					int multishotIndex = values.indexOf(new JsonPrimitive("minecraft:multishot"));
					if (multishotIndex != -1) values.add(multishotIndex + 1, new JsonPrimitive("rearm:backstep"));
					int baneOfArthropodsIndex = values.indexOf(new JsonPrimitive("minecraft:bane_of_arthropods"));
					if (baneOfArthropodsIndex != -1) values.add(baneOfArthropodsIndex + 1, new JsonPrimitive("rearm:crippling_throw"));
					int projectileProtectionIndex = values.indexOf(new JsonPrimitive("minecraft:projectile_protection"));
					if (projectileProtectionIndex != -1) values.add(projectileProtectionIndex + 1, new JsonPrimitive("rearm:magic_protection"));
					int unbreakingIndex = values.indexOf(new JsonPrimitive("minecraft:projectile_protection"));
					if (unbreakingIndex != -1) values.add(unbreakingIndex - 1, new JsonPrimitive("rearm:bash"));
					JsonArray newValues = new JsonArray();
					values.forEach(newValues::add);
					context.getFile().getAsJsonObject().add("values", newValues);
				}
		);

		// Language files
		MixsonHelper.registerMultiJsonPersistent(
				"Apply enchantment name overrides",
				index -> index.id().toString().startsWith("minecraft:lang/"),
				context -> {
					if (context.getIndex().id().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
						if (ReArm.CONFIG.protection.elementalProtection.get()) {
							context.getFile().getAsJsonObject().remove("enchantment.minecraft.fire_protection");
						}
						if (ReArm.CONFIG.protection.meleeProtection.get()) {
							context.getFile().getAsJsonObject().remove("enchantment.minecraft.protection");
						}
					}
				}
		);
		if (CompatFlags.ENCHDESC_MOD_LOADED) {
			CompatFlags.ENCHANTMENT_DESCRIPTION_MODS.forEach((mod, suffix) -> MixsonHelper.registerMultiJsonPersistent(
					"Apply enchantment description overrides for " + mod,
					index -> index.id().toString().startsWith(mod.replace('-', '_') + ":lang/"),
					context -> {
						if (context.getIndex().id().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
							try {
								if (ReArm.CONFIG.bow.improvedMultishot.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.multishot." + suffix);
								}
								if (ReArm.CONFIG.crossbow.improvedPiercing.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.piercing." + suffix);
								}
								if (ReArm.CONFIG.sword.improvedSweepingEdge.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.sweeping_edge." + suffix);
								}
								if (ReArm.CONFIG.protection.elementalProtection.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.fire_protection." + suffix);
								}
								if (ReArm.CONFIG.protection.meleeProtection.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.protection." + suffix);
								}
								if (ReArm.CONFIG.tweaks.infinityFix.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.infinity." + suffix);
								}
								if (ReArm.CONFIG.crossbow.acceptPower.get()) {
									context.getFile().getAsJsonObject().remove("enchantment.minecraft.power." + suffix);
								}
								if (context.getFile().getAsJsonObject().has("enchdesc.activate.message")) {
									context.getFile().getAsJsonObject().remove("enchdesc.activate.message");
								}
							} catch (NullPointerException ignored) {}
						}
					}
			));
		}
	}
}
