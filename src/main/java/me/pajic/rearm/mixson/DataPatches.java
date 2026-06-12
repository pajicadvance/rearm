package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.ReArm;
import net.ramixin.mixson.EventContext;
import net.ramixin.mixson.util.Index;

import java.util.List;

public class DataPatches {

	public static void init() {
		// Enchantments
		MixsonHelper.registerSingleJson(
				"Change Infinity supported items",
				new Index("minecraft:enchantment/infinity"),
				context -> context.getFile().getAsJsonObject()
						.addProperty("supported_items", "#minecraft:enchantable/infinity_enchantable")
		);
		MixsonHelper.registerSingleJson(
				"Change Knockback supported items",
				new Index("minecraft:enchantment/knockback"),
				context -> context.getFile().getAsJsonObject()
						.addProperty("supported_items", "#minecraft:enchantable/knockback_enchantable")
		);
		MixsonHelper.registerSingleJson(
				"Change Looting supported items",
				new Index("minecraft:enchantment/looting"),
				context -> {
					if (ReArm.CONFIG.axe.acceptLooting.get()) {
						context.getFile().getAsJsonObject()
								.addProperty("supported_items", "#minecraft:enchantable/sharp_weapon");
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Change Loyalty supported items",
				new Index("minecraft:enchantment/loyalty"),
				context -> context.getFile().getAsJsonObject()
						.addProperty("supported_items", "#minecraft:enchantable/loyalty_enchantable")
		);
		MixsonHelper.registerSingleJson(
				"Modify Multishot enchantment",
				new Index("minecraft:enchantment/multishot"),
				context -> {
					context.getFile().getAsJsonObject()
							.addProperty("supported_items", "#minecraft:enchantable/multishot_enchantable");
					if (ReArm.CONFIG.bow.improvedMultishot.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonObject("effects")
								.getAsJsonArray("minecraft:projectile_count").get(0).getAsJsonObject()
								.getAsJsonObject("effect")
								.getAsJsonObject("value")
								.addProperty("per_level_above_first", ReArm.CONFIG.bow.additionalArrowsPerLevel.get());
						context.getFile().getAsJsonObject()
								.addProperty("max_level", ReArm.CONFIG.bow.maxMultishotLevel.get());
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Change Power supported items",
				new Index("minecraft:enchantment/power"),
				context -> {
					context.getFile().getAsJsonObject()
							.addProperty("supported_items", "#minecraft:enchantable/power_enchantable");
					JsonObject effects = context.getFile().getAsJsonObject().getAsJsonObject("effects");
					if (ReArm.CONFIG.bugFixes.powerDamageFix.get() && effects.has("minecraft:damage")) effects
							.getAsJsonArray("minecraft:damage").get(0).getAsJsonObject()
							.getAsJsonObject("effect")
							.getAsJsonObject("value")
							.addProperty("base", 1.0);
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Impaling enchantment",
				new Index("minecraft:enchantment/impaling"),
				context -> {
					if (ReArm.CONFIG.trident.improvedImpaling.get()) {
						JsonObject effects = context.getFile().getAsJsonObject().getAsJsonObject("effects");
						if (effects.has("minecraft:damage")) {
							JsonArray damage = effects.getAsJsonArray("minecraft:damage");
							damage.forEach(element -> {
								JsonObject object = element.getAsJsonObject();
								if (object.has("requirements")) {
									JsonElement reqElem = object.get("requirements");
									if (reqElem.isJsonObject()) {
										JsonObject requirements = reqElem.getAsJsonObject();
										JsonObject match = new JsonObject();
										match.addProperty("condition", "minecraft:entity_properties");
										match.addProperty("entity", "this");
										JsonObject predicate = new JsonObject();
										predicate.addProperty(/*? 26.1.2 {*//*"type"*//*?} else {*/"minecraft:entity_type"/*?}*/, "#minecraft:sensitive_to_impaling");
										match.add("predicate", predicate);
										if (requirements.equals(match)) {
											JsonElement newRequirements = JsonParser.parseString("""
											{
												"condition": "minecraft:any_of",
												"terms": [
												 {
												   "condition": "minecraft:entity_properties",
												   "entity": "this",
												   "predicate": {}
												 },
												 {
												   "condition": "minecraft:entity_properties",
												   "entity": "this",
												   "predicate": {}
												 }
												]
											}
											""");
											JsonArray terms = newRequirements.getAsJsonObject().getAsJsonArray("terms");
											terms.get(0).getAsJsonObject().getAsJsonObject("predicate").addProperty(/*? 26.1.2 {*//*"type"*//*?} else {*/"minecraft:entity_type"/*?}*/, "#minecraft:sensitive_to_impaling");
											//? 26.1.2 {
											/*JsonObject typeSpecific = new JsonObject();
											typeSpecific.addProperty("type", "rearm:is_in_water_or_rain");
											terms.get(1).getAsJsonObject().getAsJsonObject("predicate").add("type_specific", typeSpecific);
											*///?} else {
											terms.get(1).getAsJsonObject().getAsJsonObject("predicate").add("rearm:is_in_water_or_rain", new JsonObject());
											//?}
											object.add("requirements", newRequirements);
										}
									}
								}
							});
						}
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Transform Fire Protection into Elemental Protection",
				new Index("minecraft:enchantment/fire_protection"),
				context -> {
					normalizeEnchantmentCosts(context);
					if (ReArm.CONFIG.protection.elementalProtection.get()) {
						JsonArray tags = new JsonArray();
						JsonObject tag1 = new JsonObject();
						tag1.addProperty("expected", true);
						tag1.addProperty("id", "minecraft:is_elemental");
						JsonObject tag2 = new JsonObject();
						tag2.addProperty("expected", false);
						tag2.addProperty("id", "minecraft:bypasses_invulnerability");
						tags.add(tag1);
						tags.add(tag2);

						JsonObject reqs = context.getFile().getAsJsonObject()
								.getAsJsonObject("effects")
								.getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
								.getAsJsonObject("requirements");
						if (reqs.has("terms")) {
							reqs.getAsJsonArray("terms").get(0).getAsJsonObject()
									.getAsJsonObject("predicate")
									.add("tags", tags);
						} else {
							reqs.getAsJsonObject("predicate").add("tags", tags);
						}
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Transform Protection into Melee Protection",
				new Index("minecraft:enchantment/protection"),
				context -> {
					normalizeEnchantmentCosts(context);
					if (
							ReArm.CONFIG.protection.meleeProtection.get() &&
									context.getFile().getAsJsonObject()
											.getAsJsonObject("effects")
											.has("minecraft:damage_protection")
					) {
						JsonArray tags = new JsonArray();
						JsonObject tag1 = new JsonObject();
						tag1.addProperty("expected", false);
						tag1.addProperty("id", "minecraft:bypasses_invulnerability");
						JsonObject tag2 = new JsonObject();
						tag2.addProperty("expected", false);
						tag2.addProperty("id", "minecraft:is_elemental");
						JsonObject tag3 = new JsonObject();
						tag3.addProperty("expected", false);
						tag3.addProperty("id", "minecraft:is_explosion");
						JsonObject tag4 = new JsonObject();
						tag4.addProperty("expected", false);
						tag4.addProperty("id", "minecraft:is_projectile");
						JsonObject tag5 = new JsonObject();
						tag5.addProperty("expected", false);
						tag5.addProperty("id", "minecraft:bypasses_armor");
						tags.add(tag1);
						tags.add(tag2);
						tags.add(tag3);
						tags.add(tag4);
						tags.add(tag5);

						JsonObject damageProtection = context.getFile().getAsJsonObject()
								.getAsJsonObject("effects")
								.getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject();
						damageProtection.getAsJsonObject("requirements")
								.getAsJsonObject("predicate")
								.add("tags", tags);
						JsonObject value = damageProtection.getAsJsonObject("effect")
								.getAsJsonObject("value");
						value.addProperty("base", 2.0);
						value.addProperty("per_level_above_first", 2.0);
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Normalize Projectile Protection cost",
				new Index("minecraft:enchantment/projectile_protection"),
				DataPatches::normalizeEnchantmentCosts
		);
		MixsonHelper.registerSingleJson(
				"Normalize Blast Protection cost",
				new Index("minecraft:enchantment/blast_protection"),
				DataPatches::normalizeEnchantmentCosts
		);
		MixsonHelper.registerSingleJson(
				"Set Backstep effect values",
				new Index("rearm:enchantment/backstep"),
				context -> {
					if (ReArm.CONFIG.bow.enableBackstep.get()) {
						JsonObject postAttackEffect = context.getFile().getAsJsonObject()
								.getAsJsonObject("effects")
								.getAsJsonArray("minecraft:post_attack").get(0).getAsJsonObject()
								.getAsJsonObject("effect");
						postAttackEffect.addProperty("min_duration", ReArm.CONFIG.bow.backstepTimeframe.get() / 20.0F);
						postAttackEffect.addProperty("max_duration", ReArm.CONFIG.bow.backstepTimeframe.get() / 20.0F);
					}
				}
		);

		// Enchantable tags
		MixsonHelper.registerSingleJson(
				"Modify Infinity Enchantable tag",
				new Index("minecraft:tags/item/enchantable/infinity_enchantable"),
				context -> {
					if (ReArm.CONFIG.crossbow.acceptInfinity.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/crossbow");
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Knockback Enchantable tag",
				new Index("minecraft:tags/item/enchantable/knockback_enchantable"),
				context -> {
					if (ReArm.CONFIG.axe.acceptKnockback.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/axe");
					}
					if (ReArm.CONFIG.sword.rejectKnockback.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.remove(new JsonPrimitive("#minecraft:enchantable/sword"));
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Multishot Enchantable tag",
				new Index("minecraft:tags/item/enchantable/multishot_enchantable"),
				context -> {
					if (ReArm.CONFIG.bow.acceptMultishot.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/bow");
					}

					if (ReArm.CONFIG.crossbow.rejectMultishot.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.remove(new JsonPrimitive("#minecraft:enchantable/crossbow"));
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Power Enchantable tag",
				new Index("minecraft:tags/item/enchantable/power_enchantable"),
				context -> {
					if (ReArm.CONFIG.crossbow.acceptPower.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/crossbow");
					}
				}
		);
		MixsonHelper.registerSingleJson(
				"Modify Loyalty Enchantable tag",
				new Index("minecraft:tags/item/enchantable/loyalty_enchantable"),
				context -> {
					if (ReArm.CONFIG.axe.requireLoyaltyForRecall.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/axe");
					}
				}
		);

		// Enchantment exclusive set tags
		MixsonHelper.registerSingleJson(
				"Allow Mending with Infinity",
				new Index("minecraft:tags/enchantment/exclusive_set/bow"),
				context -> {
					if (ReArm.CONFIG.tweaks.infinimending.get()) {
						List<JsonElement> values = context.getFile().getAsJsonObject().getAsJsonArray("values").asList();
						JsonElement infinity = new JsonPrimitive("minecraft:infinity");
						JsonElement mending = new JsonPrimitive("minecraft:mending");
						if (values.contains(infinity) && values.contains(mending)) {
							values.remove(infinity);
							values.remove(mending);
						}
						JsonArray newValues = new JsonArray();
						values.forEach(newValues::add);
						context.getFile().getAsJsonObject().add("values", newValues);
					}
				}
		);

		// Recipes
		if (ReArm.CONFIG.tweaks.craftTippedArrowsWithRegularPotions.get())
			MixsonHelper.registerSingleJson(
					"Modify tipped arrow recipe",
					new Index("minecraft:recipe/tipped_arrow"),
					context -> context.getFile().getAsJsonObject().addProperty("source", "minecraft:potion")
			);

		// Recipe viewer stuff
		if (ReArm.CONFIG.tweaks.hideDisabledItemsFromRecipeViewers.get()) MixsonHelper.registerSingleJson(
				"Hide disabled items from recipe viewers",
				new Index("c:tags/item/hidden_from_recipe_viewers"),
				context -> {
					if (!ReArm.CONFIG.crossbow.crossbowNetheriteVariant.get()) {
						context.getFile().getAsJsonObject().getAsJsonArray("values").add("rearm:netherite_crossbow");
					}
					if (!ReArm.CONFIG.bow.bowNetheriteVariant.get()) {
						context.getFile().getAsJsonObject().getAsJsonArray("values").add("rearm:netherite_bow");
					}
					if (!ReArm.CONFIG.shield.shieldNetheriteVariant.get()) {
						context.getFile().getAsJsonObject().getAsJsonArray("values").add("rearm:netherite_shield");
					}
				}
		);
	}

	private static void normalizeEnchantmentCosts(EventContext<JsonElement> context) {
		if (ReArm.CONFIG.protection.normalizeEnchantmentCosts.get()) {
			context.getFile().getAsJsonObject()
					.getAsJsonObject("max_cost")
					.addProperty("base", 12);
			context.getFile().getAsJsonObject()
					.getAsJsonObject("max_cost")
					.addProperty("per_level_above_first", 11);
			context.getFile().getAsJsonObject()
					.getAsJsonObject("min_cost")
					.addProperty("base", 5);
			context.getFile().getAsJsonObject()
					.getAsJsonObject("min_cost")
					.addProperty("per_level_above_first", 10);
		}
	}
}
