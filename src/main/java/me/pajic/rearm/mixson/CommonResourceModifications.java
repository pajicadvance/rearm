package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.ReArm;
import net.ramixin.mixson.debug.DebugMode;
import net.ramixin.mixson.inline.EventContext;
import net.ramixin.mixson.inline.Mixson;

import java.util.List;

@SuppressWarnings("removal")
public class CommonResourceModifications {
	private static boolean initialized = false;

    public static void init() {
		if (initialized) return;
        if (ReArm.xplat().isDebug()) Mixson.setDebugMode(DebugMode.EXPORT);

        // Enchantments
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/infinity",
                "Change Infinity supported items",
                context -> context.getFile().getAsJsonObject()
                        .addProperty("supported_items", "#minecraft:enchantable/infinity_enchantable")
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/knockback",
                "Change Knockback supported items",
                context -> context.getFile().getAsJsonObject()
                        .addProperty("supported_items", "#minecraft:enchantable/knockback_enchantable")
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/looting",
                "Change Looting supported items",
                context -> {
                    if (ReArm.CONFIG.axe.acceptLooting.get()) {
                        context.getFile().getAsJsonObject()
                                .addProperty("supported_items", "#minecraft:enchantable/sharp_weapon");
                    }
                }
        );
		Mixson.registerEvent(
				Mixson.DEFAULT_PRIORITY,
				"minecraft:enchantment/loyalty",
				"Change Loyalty supported items",
				context -> context.getFile().getAsJsonObject()
						.addProperty("supported_items", "#minecraft:enchantable/loyalty_enchantable")
		);
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/multishot",
                "Modify Multishot enchantment",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/power",
                "Change Power supported items",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/fire_protection",
                "Transform Fire Protection into Elemental Protection",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/protection",
                "Transform Protection into Melee Protection",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/projectile_protection",
                "Normalize Projectile Protection cost",
                CommonResourceModifications::normalizeEnchantmentCosts
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/blast_protection",
                "Normalize Blast Protection cost",
                CommonResourceModifications::normalizeEnchantmentCosts
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "rearm:enchantment/backstep",
                "Set Backstep effect values",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/infinity_enchantable",
                "Modify Infinity Enchantable tag",
                context -> {
                    if (ReArm.CONFIG.crossbow.acceptInfinity.get()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/knockback_enchantable",
                "Modify Knockback Enchantable tag",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/multishot_enchantable",
                "Modify Multishot Enchantable tag",
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
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/power_enchantable",
                "Modify Power Enchantable tag",
                context -> {
                    if (ReArm.CONFIG.crossbow.acceptPower.get()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                }
        );
		Mixson.registerEvent(
				Mixson.DEFAULT_PRIORITY,
				"minecraft:tags/item/enchantable/loyalty_enchantable",
				"Modify Loyalty Enchantable tag",
				context -> {
					if (ReArm.CONFIG.axe.requireLoyaltyForRecall.get()) {
						context.getFile().getAsJsonObject()
								.getAsJsonArray("values")
								.add("#minecraft:enchantable/axe");
					}
				}
		);

        // Enchantment exclusive set tags
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/enchantment/exclusive_set/bow",
                "Allow Mending with Infinity",
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

		// Recipe viewer stuff
		if (ReArm.CONFIG.tweaks.hideDisabledItemsFromRecipeViewers.get()) Mixson.registerEvent(
				Mixson.DEFAULT_PRIORITY,
				"c:tags/item/hidden_from_recipe_viewers",
				"Hide disabled items from recipe viewers",
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

		initialized = true;
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
