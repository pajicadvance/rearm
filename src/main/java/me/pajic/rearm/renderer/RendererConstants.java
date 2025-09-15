package me.pajic.rearm.renderer;

import me.pajic.rearm.Main;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class RendererConstants {
    public static final ResourceLocation NETHERITE_SHIELD_SHEET = ResourceLocation.fromNamespaceAndPath(
            Main.MOD_ID, "textures/atlas/netherite_shield_patterns.png"
    );
    public static final ResourceLocation NETHERITE_SHIELD_PATTERNS = ResourceLocation.fromNamespaceAndPath(
            Main.MOD_ID, "netherite_shield_patterns"
    );
    public static final Material NETHERITE_SHIELD_BASE = new Material(
            NETHERITE_SHIELD_SHEET,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "entity/netherite_shield_base")
    );
    public static final Material NO_PATTERN_NETHERITE_SHIELD = new Material(
            NETHERITE_SHIELD_SHEET,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "entity/netherite_shield_base_nopattern")
    );
    public static final ModelLayerLocation NETHERITE_SHIELD_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_shield"), "main"
    );
}
