package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.Main;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.ramixin.mixson.DebugMode;
import net.ramixin.mixson.Mixson;

import java.util.List;

public class ResourceModifications {

    private static final List<String> ENCHANTMENT_DESCRIPTION_MODS = List.of(
            "enchdesc",
            "idwtialsimmoedm"
    );

    public static void init() {

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Mixson.setDebugMode(DebugMode.EXPORT);

        // Enchantments
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/infinity"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_infinity"),

                jsonElement -> {
                    jsonElement.getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/infinity_enchantable");
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/knockback"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_knockback"),

                jsonElement -> {
                    jsonElement.getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/knockback_enchantable");
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/looting"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_looting"),

                jsonElement -> {
                    if (Main.CONFIG.axe.acceptLooting()) {
                        jsonElement.getAsJsonObject()
                                .addProperty("supported_items", "#minecraft:enchantable/sharp_weapon");
                    }
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/multishot"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_multishot"),

                jsonElement -> {
                    jsonElement.getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/multishot_enchantable");

                    if (Main.CONFIG.multishot.multishotAbility()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:projectile_count").get(0).getAsJsonObject()
                                .getAsJsonObject("effect")
                                .getAsJsonObject("value")
                                .addProperty("base", Main.CONFIG.multishot.multishotAdditionalArrows());
                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:projectile_spread").get(0).getAsJsonObject()
                                .getAsJsonObject("effect")
                                .getAsJsonObject("value")
                                .addProperty("base", Main.CONFIG.multishot.multishotAdditionalArrows() * 5);
                    }

                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/power"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_power"),

                jsonElement -> {
                    jsonElement.getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/power_enchantable");

                    jsonElement.getAsJsonObject()
                            .getAsJsonObject("effects")
                            .getAsJsonArray("minecraft:damage").get(0).getAsJsonObject()
                            .getAsJsonObject("effect")
                            .getAsJsonObject("value")
                            .addProperty("base", 1.0);

                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/sweeping_edge"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_sweeping_edge"),

                jsonElement -> {
                    if (Main.CONFIG.sweepingEdge.sweepingEdgeAbility()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .remove("minecraft:attributes");
                    }
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/projectile_protection"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_projectile_protection"),

                jsonElement -> {
                    if (Main.CONFIG.piercingShot.piercingShotAbility()) {
                        JsonObject condition = new JsonObject();
                        condition.addProperty("condition", "minecraft:damage_source_properties");
                        JsonObject tags = new JsonObject();
                        JsonArray tagArray = new JsonArray();
                        JsonObject tag1 = new JsonObject();
                        tag1.addProperty("expected", true);
                        tag1.addProperty("id", "rearm:is_piercing_arrow");
                        JsonObject tag2 = new JsonObject();
                        tag2.addProperty("expected", true);
                        tag2.addProperty("id", "minecraft:bypasses_armor");
                        JsonObject tag3 = new JsonObject();
                        tag3.addProperty("expected", false);
                        tag3.addProperty("id", "minecraft:bypasses_invulnerability");
                        tagArray.add(tag1);
                        tagArray.add(tag2);
                        tagArray.add(tag3);
                        tags.add("tags", tagArray);
                        condition.add("predicate", tags);

                        JsonObject requirements = new JsonObject();
                        requirements.addProperty("condition", "minecraft:any_of");
                        JsonArray termArray = new JsonArray();
                        termArray.add(jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .getAsJsonObject("requirements"));
                        termArray.add(condition);
                        requirements.add("terms", termArray);

                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .add("requirements", requirements);
                    }
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/fire_protection"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_fire_protection"),

                jsonElement -> {
                    if (Main.CONFIG.elementalProtection()) {
                        JsonArray tags = new JsonArray();
                        JsonObject tag1 = new JsonObject();
                        tag1.addProperty("expected", true);
                        tag1.addProperty("id", "rearm:is_elemental");
                        JsonObject tag2 = new JsonObject();
                        tag2.addProperty("expected", false);
                        tag2.addProperty("id", "minecraft:bypasses_invulnerability");
                        tags.add(tag1);
                        tags.add(tag2);

                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .getAsJsonObject("requirements")
                                .getAsJsonArray("terms").get(0).getAsJsonObject()
                                .getAsJsonObject("predicate")
                                .add("tags", tags);
                    }

                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("enchantment/protection"),
                ResourceLocation.fromNamespaceAndPath("rearm", "protection"),

                jsonElement -> {
                    if (Main.CONFIG.meleeProtection()) {
                        JsonArray tags = new JsonArray();
                        JsonObject tag1 = new JsonObject();
                        tag1.addProperty("expected", false);
                        tag1.addProperty("id", "minecraft:bypasses_invulnerability");
                        JsonObject tag2 = new JsonObject();
                        tag2.addProperty("expected", false);
                        tag2.addProperty("id", "rearm:is_elemental");
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

                        jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .getAsJsonObject("requirements")
                                .getAsJsonObject("predicate")
                                .add("tags", tags);
                    }
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.fromNamespaceAndPath("rearm", "enchantment/backstep"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_backstep"),

                jsonElement -> {
                    if (Main.CONFIG.bow.enableBackstep()) {
                        JsonObject postAttackEffect = jsonElement.getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:post_attack").get(0).getAsJsonObject()
                                .getAsJsonObject("effect");
                        postAttackEffect.addProperty("min_duration", Main.CONFIG.bow.backstepTimeframe() / 20.0F);
                        postAttackEffect.addProperty("max_duration", Main.CONFIG.bow.backstepTimeframe() / 20.0F);
                    }
                    return jsonElement;
                }
        );

        // Enchantable tags
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/item/enchantable/infinity_enchantable"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_infinity_enchantable"),

                jsonElement -> {
                    if (Main.CONFIG.crossbow.acceptInfinity()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/item/enchantable/knockback_enchantable"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_knockback_enchantable"),

                jsonElement -> {
                    if (Main.CONFIG.axe.acceptKnockback()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/axe");
                    }

                    if (Main.CONFIG.sword.rejectKnockback()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .remove(new JsonPrimitive("#minecraft:enchantable/sword"));
                    }

                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/item/enchantable/multishot_enchantable"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_multishot_enchantable"),

                jsonElement -> {
                    if (Main.CONFIG.bow.acceptMultishot()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/bow");
                    }

                    if (Main.CONFIG.crossbow.rejectMultishot()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .remove(new JsonPrimitive("#minecraft:enchantable/crossbow"));
                    }

                    return jsonElement;
                }
        );
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/item/enchantable/power_enchantable"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_power_enchantable"),

                jsonElement -> {
                    if (Main.CONFIG.crossbow.acceptPower()) {
                        jsonElement.getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                    return jsonElement;
                }
        );

        // Enchantment exclusive set tags
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/enchantment/exclusive_set/bow"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_bow_exclusive_set"),

                jsonElement -> {
                    if (Main.CONFIG.infinimending()) {
                        List<JsonElement> values = jsonElement.getAsJsonObject().getAsJsonArray("values").asList();
                        JsonElement infinity = new JsonPrimitive("minecraft:infinity");
                        JsonElement mending = new JsonPrimitive("minecraft:mending");
                        if (values.contains(infinity) && values.contains(mending)) {
                            values.remove(infinity);
                            values.remove(mending);
                        }
                        JsonArray newValues = new JsonArray();
                        values.forEach(newValues::add);
                        jsonElement.getAsJsonObject().add("values", newValues);
                    }
                    return jsonElement;
                }
        );

        // Enchantment tooltip order
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("tags/enchantment/tooltip_order"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_enchantment_tooltip_order"),

                jsonElement -> {
                    List<JsonElement> values = jsonElement.getAsJsonObject().getAsJsonArray("values").asList();
                    int multishotIndex = values.indexOf(new JsonPrimitive("minecraft:multishot"));
                    values.add(multishotIndex + 1, new JsonPrimitive("rearm:backstep"));
                    int baneOfArthropodsIndex = values.indexOf(new JsonPrimitive("minecraft:bane_of_arthropods"));
                    values.add(baneOfArthropodsIndex + 1, new JsonPrimitive("rearm:crippling_blow"));
                    int projectileProtectionIndex = values.indexOf(new JsonPrimitive("minecraft:projectile_protection"));
                    values.add(projectileProtectionIndex + 1, new JsonPrimitive("rearm:magic_protection"));
                    JsonArray newValues = new JsonArray();
                    values.forEach(newValues::add);
                    jsonElement.getAsJsonObject().add("values", newValues);
                    return jsonElement;
                }
        );

        // Language files
        Mixson.registerModificationEvent(
                ResourceLocation.withDefaultNamespace("lang/en_us"),
                ResourceLocation.fromNamespaceAndPath("rearm", "modify_lang"),

                jsonElement -> {
                    if (Main.CONFIG.piercingShot.piercingShotAbility()) {
                        jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.piercing",
                                "Piercing Shot"
                        );
                    }
                    if (Main.CONFIG.elementalProtection()) {
                        jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.fire_protection",
                                "Elemental Protection"
                        );
                    }
                    if (Main.CONFIG.meleeProtection()) {
                        jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.protection",
                                "Melee Protection"
                        );
                    }
                    return jsonElement;
                }
        );
        ENCHANTMENT_DESCRIPTION_MODS.forEach(mod -> {
            if (FabricLoader.getInstance().isModLoaded(mod)) Mixson.registerModificationEvent(
                    ResourceLocation.fromNamespaceAndPath(mod,"lang/en_us"),
                    ResourceLocation.fromNamespaceAndPath("rearm", "modify_lang_" + mod),

                    jsonElement -> {
                        if (Main.CONFIG.multishot.multishotAbility()) jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.multishot.desc",
                                "Ability: The next shot will fire multiple spread out arrows."
                        );
                        if (Main.CONFIG.piercingShot.piercingShotAbility()) jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.piercing.desc",
                                "Ability: The next arrow shot will pierce through enemies and ignore their armor."
                        );
                        if (Main.CONFIG.sweepingEdge.sweepingEdgeAbility()) jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.sweeping_edge.desc",
                                "Ability: The next attack will strike all enemies in a moderate radius around you and deal increased damage to all enemies based on the amount of enemies hit."
                        );
                        if (Main.CONFIG.elementalProtection()) jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.fire_protection.desc",
                                "High resistance to fire, lightning and freeze damage and reduced burn time if you're set ablaze"
                        );
                        if (Main.CONFIG.meleeProtection()) jsonElement.getAsJsonObject().addProperty(
                                "enchantment.minecraft.protection.desc",
                                "Moderate damage resistance to most close-up physical damage sources"
                        );
                        return jsonElement;
                    }
            );
        });
    }
}
