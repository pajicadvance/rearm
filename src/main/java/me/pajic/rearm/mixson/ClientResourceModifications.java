package me.pajic.rearm.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import me.pajic.rearm.CompatFlags;
import me.pajic.rearm.Main;
import net.minecraft.client.Minecraft;
import net.ramixin.mixson.inline.Mixson;
import net.ramixin.mixson.util.MixsonUtil;

import java.util.List;
import java.util.Map;

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
                MixsonUtil.getLocatorFromString("rearm:lang/*"),
                "rearm:modify_lang",
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
        Object2BooleanMap<String> KEYS = new Object2BooleanArrayMap<>(Map.ofEntries(
                Map.entry("enchantment.minecraft.multishot.desc", Main.CONFIG.bow.improvedMultishot.get()),
                Map.entry("enchantment.minecraft.piercing.desc", Main.CONFIG.crossbow.improvedPiercing.get()),
                Map.entry("enchantment.minecraft.sweeping_edge.desc", Main.CONFIG.sword.improvedSweepingEdge.get()),
                Map.entry("enchantment.minecraft.fire_protection.desc", Main.CONFIG.protection.elementalProtection.get()),
                Map.entry("enchantment.minecraft.protection.desc", Main.CONFIG.protection.meleeProtection.get()),
                Map.entry("enchantment.minecraft.infinity.desc", Main.CONFIG.tweaks.infinityFix.get()),
                Map.entry("enchantment.minecraft.power.desc", Main.CONFIG.crossbow.acceptPower.get())
        ));
        if (CompatFlags.ENCHDESC_MOD_LOADED) CompatFlags.ENCHANTMENT_DESCRIPTION_MODS.forEach(mod -> Mixson.registerEvent(
                Mixson.DEFAULT_PRIORITY,
                MixsonUtil.getLocatorFromString(mod + ":lang/*"),
                "rearm:modify_lang_" + mod,
                context -> {
                    if (context.getResourceId().getPath().contains(Minecraft.getInstance().getLanguageManager().getSelected())) {
                        try {
                            KEYS.object2BooleanEntrySet().forEach(entry -> {
                                if (entry.getBooleanValue()) {
                                    context.getFile().getAsJsonObject().addProperty(
                                            entry.getKey(),
                                            context.getFile().getAsJsonObject().get(entry.getKey() + ".override").getAsString()
                                    );
                                }
                            });
                            context.getFile().getAsJsonObject().addProperty(
                                    "enchdesc.activate.message",
                                    context.getFile().getAsJsonObject().get("enchdesc.activate.message.override").getAsString()
                            );
                        } catch (NullPointerException ignored) {}
                    }
                },
                true
        ));
    }
}
