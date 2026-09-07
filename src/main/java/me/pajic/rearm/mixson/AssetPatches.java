package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.CompatFlags;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.util.Index;

import java.util.List;

//? <26.1 {
/*import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
*///?}

public class AssetPatches {

    //? <26.1 {
    /*private static final JsonElement netheriteBowOverride = JsonParser.parseString("""
            [
                {
                  "predicate": {
                    "pulling": 1
                  },
                  "model": "rearm:item/netherite_bow_pulling_0"
                },
                {
                  "predicate": {
                    "pulling": 1,
                    "pull": 0.65
                  },
                  "model": "rearm:item/netherite_bow_pulling_1"
                },
                {
                  "predicate": {
                    "pulling": 1,
                    "pull": 0.9
                  },
                  "model": "rearm:item/netherite_bow_pulling_2"
                }
            ]""");

    private static final JsonElement netheriteCrossbowOverride = JsonParser.parseString("""
            [
                {
                    "predicate": {
                        "pulling": 1
                    },
                    "model": "rearm:item/netherite_crossbow_pulling_0"
                },
                {
                    "predicate": {
                        "pulling": 1,
                        "pull": 0.58
                    },
                    "model": "rearm:item/netherite_crossbow_pulling_1"
                },
                {
                    "predicate": {
                        "pulling": 1,
                        "pull": 1.0
                    },
                    "model": "rearm:item/netherite_crossbow_pulling_2"
                },
                {
                    "predicate": {
                        "charged": 1
                    },
                    "model": "rearm:item/netherite_crossbow_arrow"
                },
                {
                    "predicate": {
                        "charged": 1,
                        "firework": 1
                    },
                    "model": "rearm:item/netherite_crossbow_firework"
                }
            ]""");

    private static final JsonElement netheriteShieldOverride = JsonParser.parseString("""
            [
                {
                    "predicate": {
                        "blocking": 1
                    },
                    "model": "rearm:item/netherite_shield_blocking"
                }
            ]""");
    *///?}

	public static void init() {
		// Enchantment tooltip order
		MixsonHelper.registerSingleJson(
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
		MixsonHelper.registerMultiJson(
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
			CompatFlags.ENCHANTMENT_DESCRIPTION_MODS.forEach((mod, suffix) -> MixsonHelper.registerMultiJson(
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

        // Item models
        //? <26.1 {
        /*MixsonHelper.registerSingleJson(
                "Patch netherite bow model for pre 26.1",
                new Index("rearm:models/item/netherite_bow"),
                context -> context.getFile().getAsJsonObject().add("overrides", netheriteBowOverride)
        );
        MixsonHelper.registerSingleJson(
                "Patch netherite crossbow model for pre 26.1",
                new Index("rearm:models/item/netherite_crossbow"),
                context -> context.getFile().getAsJsonObject().add("overrides", netheriteCrossbowOverride)
        );
        MixsonHelper.registerSingleJson(
                "Patch netherite shield model for pre 26.1",
                new Index("rearm:models/item/netherite_shield"),
                context -> {
                    JsonObject obj = context.getFile().getAsJsonObject();
                    obj.addProperty("parent", "builtin/entity");
                    obj.add("overrides", netheriteShieldOverride);
                }
        );
        MixsonHelper.registerSingleJson(
                "Patch netherite shield blocking model for pre 26.1",
                new Index("rearm:models/item/netherite_shield_blocking"),
                context -> context.getFile().getAsJsonObject().addProperty("parent", "builtin/entity")
        );
        *///?}
	}
}
