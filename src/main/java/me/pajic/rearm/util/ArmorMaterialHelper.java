package me.pajic.rearm.util;

import com.google.common.collect.Maps;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.platform.MultiLoaderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//~ if <26.1 'item.equipment.ArmorType' -> 'item.ArmorItem'
import net.minecraft.world.item.equipment.ArmorType;

//~ if <26.1 'ArmorType' -> 'ArmorItem.Type' {
public class ArmorMaterialHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger("ReArm Armor Material Helper");
    private static final Path FILE_PATH = MultiLoaderUtil.INSTANCE.getConfigDir().resolve("rearm/armor_materials.txt");
    private static final List<Identifier> ARMOR_MATERIALS = new ArrayList<>();

    public static Map<ArmorType, Integer> calculateDefenses(int targetTotal) {
        int[] percents = {
                ReArm.CONFIG.armor.chestplateArmorPercent.get(),
                ReArm.CONFIG.armor.leggingsArmorPercent.get(),
                ReArm.CONFIG.armor.helmetArmorPercent.get(),
                ReArm.CONFIG.armor.bootsArmorPercent.get()
        };
        if (Arrays.stream(percents).sum() != 100) {
            LOGGER.warn("Armor piece percentages don't add up to 100, using defaults");
            percents = new int[] {
                    ReArm.CONFIG.armor.chestplateArmorPercent.getDefault(),
                    ReArm.CONFIG.armor.leggingsArmorPercent.getDefault(),
                    ReArm.CONFIG.armor.helmetArmorPercent.getDefault(),
                    ReArm.CONFIG.armor.bootsArmorPercent.getDefault()
            };
        }
        int[] values = Arrays.stream(percents).map(percent -> Math.round(targetTotal * ((float) percent / 100))).toArray();
        int body = Math.round(targetTotal * ((float) ReArm.CONFIG.armor.bodyArmorPercent.get() / 100));
        int total = Arrays.stream(values).sum();
        if (total != targetTotal) {
            int i = 0;
            while (total != targetTotal) {
                if (total > targetTotal) {
                    values[i]--;
                    total--;
                } else {
                    values[i]++;
                    total++;
                }
                i = i == 3 ? 0 : i + 1;
            }
        }
        return Maps.newEnumMap(Map.of(
                ArmorType.HELMET, values[2],
                ArmorType.CHESTPLATE, values[0],
                ArmorType.LEGGINGS, values[1],
                ArmorType.BOOTS, values[3],
                ArmorType.BODY, body
        ));
    }

    public static float calculateKnockbackResist(int targetTotal) {
        return Mth.lerp(Math.min(1, targetTotal / (20 * ReArm.CONFIG.armor.armorMultiplier.get())), 0, 0.1F);
    }

    public static void add(Identifier rl) {
        if (!ARMOR_MATERIALS.contains(rl)) ARMOR_MATERIALS.add(rl);
    }

    public static void write() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
        } catch (IOException e) {
            LOGGER.warn("Error creating armor materials directory", e);
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
            writer.write(Component.translatable("rearm.config.armor.armorMaterialList").getString());
            writer.newLine();
            writer.newLine();
            for (Identifier rl : ARMOR_MATERIALS) {
                writer.write(rl.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.warn("Error writing armor materials to file", e);
        }
    }
}
//~}
