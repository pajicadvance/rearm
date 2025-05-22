package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.Main;
import net.fabricmc.loader.api.FabricLoader;
import net.ramixin.mixson.debug.DebugMode;
import net.ramixin.mixson.inline.Mixson;

import java.util.List;

public class ResourceModifications {

    private static final List<String> ENCHANTMENT_DESCRIPTION_MODS = List.of(
            "enchdesc",
            "idwtialsimmoedm"
    );

    public static void init() {

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Mixson.setDebugMode(DebugMode.EXPORT);

        // Enchantments
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/infinity",
                "rearm:modify_infinity",
                context -> context.getFile().getAsJsonObject()
                        .addProperty("supported_items", "#minecraft:enchantable/infinity_enchantable")
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/knockback",
                "rearm:modify_knockback",
                context -> context.getFile().getAsJsonObject()
                        .addProperty("supported_items", "#minecraft:enchantable/knockback_enchantable")
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/looting",
                "rearm:modify_looting",
                context -> {
                    if (Main.CONFIG.axe.acceptLooting()) {
                        context.getFile().getAsJsonObject()
                                .addProperty("supported_items", "#minecraft:enchantable/sharp_weapon");
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/multishot",
                "rearm:modify_multishot",
                context -> {
                    context.getFile().getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/multishot_enchantable");
                    if (Main.CONFIG.bow.improvedMultishot()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:projectile_count").get(0).getAsJsonObject()
                                .getAsJsonObject("effect")
                                .getAsJsonObject("value")
                                .addProperty("per_level_above_first", Main.CONFIG.bow.additionalArrowsPerLevel());
                        context.getFile().getAsJsonObject()
                                .addProperty("max_level", Main.CONFIG.bow.maxMultishotLevel());
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/power",
                "rearm:modify_power",
                context -> {
                    context.getFile().getAsJsonObject()
                            .addProperty("supported_items", "#minecraft:enchantable/power_enchantable");
                    context.getFile().getAsJsonObject()
                            .getAsJsonObject("effects")
                            .getAsJsonArray("minecraft:damage").get(0).getAsJsonObject()
                            .getAsJsonObject("effect")
                            .getAsJsonObject("value")
                            .addProperty("base", 1.0);
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/fire_protection",
                "rearm:modify_fire_protection",
                context -> {
                    if (Main.CONFIG.elementalProtection()) {
                        JsonArray tags = new JsonArray();
                        JsonObject tag1 = new JsonObject();
                        tag1.addProperty("expected", true);
                        tag1.addProperty("id", "minecraft:is_elemental");
                        JsonObject tag2 = new JsonObject();
                        tag2.addProperty("expected", false);
                        tag2.addProperty("id", "minecraft:bypasses_invulnerability");
                        tags.add(tag1);
                        tags.add(tag2);

                        context.getFile().getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .getAsJsonObject("requirements")
                                .getAsJsonArray("terms").get(0).getAsJsonObject()
                                .getAsJsonObject("predicate")
                                .add("tags", tags);
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:enchantment/protection",
                "rearm:protection",

                context -> {
                    if (Main.CONFIG.meleeProtection()) {
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

                        context.getFile().getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:damage_protection").get(0).getAsJsonObject()
                                .getAsJsonObject("requirements")
                                .getAsJsonObject("predicate")
                                .add("tags", tags);
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "rearm:enchantment/backstep",
                "rearm:modify_backstep",
                context -> {
                    if (Main.CONFIG.bow.enableBackstep()) {
                        JsonObject postAttackEffect = context.getFile().getAsJsonObject()
                                .getAsJsonObject("effects")
                                .getAsJsonArray("minecraft:post_attack").get(0).getAsJsonObject()
                                .getAsJsonObject("effect");
                        postAttackEffect.addProperty("min_duration", Main.CONFIG.bow.backstepTimeframe() / 20.0F);
                        postAttackEffect.addProperty("max_duration", Main.CONFIG.bow.backstepTimeframe() / 20.0F);
                    }
                }
        );

        // Enchantable tags
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/infinity_enchantable",
                "rearm:modify_infinity_enchantable",
                context -> {
                    if (Main.CONFIG.crossbow.acceptInfinity()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/knockback_enchantable",
                "rearm:modify_knockback_enchantable",
                context -> {
                    if (Main.CONFIG.axe.acceptKnockback()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/axe");
                    }
                    if (Main.CONFIG.sword.rejectKnockback()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .remove(new JsonPrimitive("#minecraft:enchantable/sword"));
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/multishot_enchantable",
                "rearm:modify_multishot_enchantable",
                context -> {
                    if (Main.CONFIG.bow.acceptMultishot()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/bow");
                    }

                    if (Main.CONFIG.crossbow.rejectMultishot()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .remove(new JsonPrimitive("#minecraft:enchantable/crossbow"));
                    }
                }
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/item/enchantable/power_enchantable",
                "rearm:modify_power_enchantable",
                context -> {
                    if (Main.CONFIG.crossbow.acceptPower()) {
                        context.getFile().getAsJsonObject()
                                .getAsJsonArray("values")
                                .add("#minecraft:enchantable/crossbow");
                    }
                }
        );

        // Enchantment exclusive set tags
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/enchantment/exclusive_set/bow",
                "rearm:modify_bow_exclusive_set",
                context -> {
                    if (Main.CONFIG.infinimending()) {
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

        // Enchantment tooltip order
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:tags/enchantment/tooltip_order",
                "rearm:modify_enchantment_tooltip_order",
                context -> {
                    List<JsonElement> values = context.getFile().getAsJsonObject().getAsJsonArray("values").asList();
                    int multishotIndex = values.indexOf(new JsonPrimitive("minecraft:multishot"));
                    values.add(multishotIndex + 1, new JsonPrimitive("rearm:backstep"));
                    int baneOfArthropodsIndex = values.indexOf(new JsonPrimitive("minecraft:bane_of_arthropods"));
                    values.add(baneOfArthropodsIndex + 1, new JsonPrimitive("rearm:crippling_throw"));
                    int projectileProtectionIndex = values.indexOf(new JsonPrimitive("minecraft:projectile_protection"));
                    values.add(projectileProtectionIndex + 1, new JsonPrimitive("rearm:magic_protection"));
                    JsonArray newValues = new JsonArray();
                    values.forEach(newValues::add);
                    context.getFile().getAsJsonObject().add("values", newValues);
                }
        );

        // Language files
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                "minecraft:lang/en_us",
                "rearm:modify_lang",
                context -> {
                    if (Main.CONFIG.elementalProtection()) {
                        context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.fire_protection",
                                "Elemental Protection"
                        );
                    }
                    if (Main.CONFIG.meleeProtection()) {
                        context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.protection",
                                "Melee Protection"
                        );
                    }
                }
        );
        ENCHANTMENT_DESCRIPTION_MODS.forEach(mod -> {
            if (FabricLoader.getInstance().isModLoaded(mod)) Mixson.registerEvent(
                    Mixson.DEFAULT_PRIORITY,
                    mod + ":lang/en_us",
                    "rearm:modify_lang_" + mod,
                    context -> {
                        if (Main.CONFIG.bow.improvedMultishot()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.multishot.desc",
                                "Fires additional arrows in similar directions based on level."
                        );
                        if (Main.CONFIG.crossbow.improvedPiercing()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.piercing.desc",
                                "Allows projectiles to pierce through mobs and ignore a percentage of their armor based on level."
                        );
                        if (Main.CONFIG.sword.improvedSweepingEdge()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.sweeping_edge.desc",
                                "Increases the damage and range of sweeping attacks based on level and the amount of enemies hit."
                        );
                        if (Main.CONFIG.elementalProtection()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.fire_protection.desc",
                                "High resistance to fire, lightning and freeze damage and reduced burn time if you're set ablaze."
                        );
                        if (Main.CONFIG.meleeProtection()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.protection.desc",
                                "Moderate damage resistance to most close-up physical damage sources."
                        );
                        if (Main.CONFIG.infinityFix()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.infinity.desc",
                                "Allows the weapon to fire normal arrows for free."
                        );
                        if (Main.CONFIG.crossbow.acceptPower()) context.getFile().getAsJsonObject().addProperty(
                                "enchantment.minecraft.power.desc",
                                "Increases the damage of projectiles fired from the weapon."
                        );
                        context.getFile().getAsJsonObject().addProperty(
                                "enchdesc.activate.message",
                                "Shift for info"
                        );
                    }
            );
        });
    }
}
