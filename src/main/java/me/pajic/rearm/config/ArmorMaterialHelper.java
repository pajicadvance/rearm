package me.pajic.rearm.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArmorMaterialHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReArm Armor Material Helper");
    private static final Path FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("rearm/armor_materials.txt");
    private static final List<ResourceLocation> ARMOR_MATERIALS = new ArrayList<>();

    public static void add(ResourceLocation rl) {
        ARMOR_MATERIALS.add(rl);
    }

    public static void write() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH.toFile()));
            for (ResourceLocation rl : ARMOR_MATERIALS) {
                writer.write(rl.toString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            LOGGER.warn("Error writing armor materials to file", e);
        }
    }
}
