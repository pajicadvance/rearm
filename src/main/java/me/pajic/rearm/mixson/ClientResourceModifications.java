package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.pajic.rearm.CompatFlags;
import me.pajic.rearm.Main;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.util.MixsonUtil;

import java.util.List;

@SuppressWarnings("removal")
public class ClientResourceModifications {
    public static void init() {
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
                rl -> rl.toString().startsWith("minecraft:lang/"),
                "Apply enchantment name overrides",
                context -> {
                    if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                        if (Main.CONFIG.protection.elementalProtection.get()) {
                            context.getFile().getAsJsonObject().remove("enchantment.minecraft.fire_protection");
                        }
                        if (Main.CONFIG.protection.meleeProtection.get()) {
                            context.getFile().getAsJsonObject().remove("enchantment.minecraft.protection");
                        }
                    }
                },
                true
        );
        Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                rl -> rl.toString().startsWith("rearm:lang/"),
                "Apply enchantment name overrides",
                context -> {
                    if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                        if (Main.CONFIG.protection.elementalProtection.get()) {
                            context.getFile().getAsJsonObject().addProperty(
                                    "enchantment.minecraft.fire_protection",
                                    context.getFile().getAsJsonObject().get("enchantment.minecraft.fire_protection.override").getAsString()
                            );
                        }
                        if (Main.CONFIG.protection.meleeProtection.get()) {
                            context.getFile().getAsJsonObject().addProperty(
                                    "enchantment.minecraft.protection",
                                    context.getFile().getAsJsonObject().get("enchantment.minecraft.protection.override").getAsString()
                            );
                        }
                    }
                },
                true
        );
        if (CompatFlags.ENCHDESC_MOD_LOADED) {
            CompatFlags.ENCHANTMENT_DESCRIPTION_MODS.forEach(mod -> Mixson.registerEvent(
                    Mixson.DEFAULT_PRIORITY,
                    MixsonUtil.getLocatorFromString(mod + ":lang/*"),
                    "Apply enchantment description overrides for " + mod,
                    context -> {
                        if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                            try {
                                if (Main.CONFIG.bow.improvedMultishot.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.multishot.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.multishot.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.crossbow.improvedPiercing.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.piercing.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.piercing.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.sword.improvedSweepingEdge.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.sweeping_edge.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.sweeping_edge.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.protection.elementalProtection.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.fire_protection.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.fire_protection.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.protection.meleeProtection.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.protection.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.protection.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.tweaks.infinityFix.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.infinity.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.infinity.desc.override").getAsString()
                                    );
                                }
                                if (Main.CONFIG.crossbow.acceptPower.get()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchantment.minecraft.power.desc",
                                            context.getFile().getAsJsonObject().get("enchantment.minecraft.power.desc.override").getAsString()
                                    );
                                }
                                if (context.getFile().getAsJsonObject().has("enchdesc.activate.message")) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            "enchdesc.activate.message",
                                            context.getFile().getAsJsonObject().get("enchdesc.activate.message.override").getAsString()
                                    );
                                }
                            } catch (NullPointerException ignored) {}
                        }
                    },
                    true
            ));
        }
    }
}
