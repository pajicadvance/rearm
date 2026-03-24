package me.pajic.rearm.util;

import com.google.common.collect.Maps;
import me.pajic.rearm.ReArm;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.equipment.ArmorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ArmorMaterialHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReArm Armor Material Helper");
    private static final Path FILE_PATH = ReArm.xplat().getConfigDir().resolve("rearm/armor_materials.txt");
    private static final List<Identifier> ARMOR_MATERIALS = new ArrayList<>();

    public static Map</*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/, Integer> calculateDefenses(int targetTotal) {
        int[] values = {
                Math.round(targetTotal * ((float) ReArm.CONFIG.armor.chestplateArmorPercent.get() / 100)),
                Math.round(targetTotal * ((float) ReArm.CONFIG.armor.leggingsArmorPercent.get() / 100)),
                Math.round(targetTotal * ((float) ReArm.CONFIG.armor.helmetArmorPercent.get() / 100)),
                Math.round(targetTotal * ((float) ReArm.CONFIG.armor.bootsArmorPercent.get() / 100))
        };
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
                /*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/.HELMET, values[2],
                /*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/.CHESTPLATE, values[0],
                /*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/.LEGGINGS, values[1],
                /*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/.BOOTS, values[3],
                /*? if 1.21.1 {*//*ArmorItem.Type*//*?} else {*/ArmorType/*?}*/.BODY, body
        ));
    }

    public static float calculateKnockbackResist(int targetTotal) {
        return Mth.lerp(targetTotal / (20 * ReArm.CONFIG.armor.armorMultiplier.get()), 0, 0.1F);
    }

    public static void add(Identifier rl) {
        ARMOR_MATERIALS.add(rl);
    }

    public static void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH.toFile()));
            writer.write(Component.translatable("rearm.config.armor.armorMaterialList").getString());
            writer.newLine();
            writer.newLine();
            for (Identifier rl : ARMOR_MATERIALS) {
                writer.write(rl.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            LOGGER.warn("Error writing armor materials to file", e);
        }
    }
}
