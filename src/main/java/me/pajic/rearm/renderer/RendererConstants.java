package me.pajic.rearm.renderer;

import me.pajic.rearm.ReArm;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public class RendererConstants {

    public static final Identifier NETHERITE_SHIELD_SHEET = ReArm.id("textures/atlas/netherite_shield_patterns.png");
    public static final Identifier NETHERITE_SHIELD_PATTERNS = ReArm.id("netherite_shield_patterns");
    public static final SpriteId NETHERITE_SHIELD_BASE = new SpriteId(
            NETHERITE_SHIELD_SHEET,
            ReArm.id("entity/netherite_shield_base")
    );
    public static final SpriteId NO_PATTERN_NETHERITE_SHIELD = new SpriteId(
            NETHERITE_SHIELD_SHEET,
            ReArm.id("entity/netherite_shield_base_nopattern")
    );
    public static final ModelLayerLocation NETHERITE_SHIELD_LAYER = new ModelLayerLocation(
            ReArm.id("netherite_shield"), "main"
    );
}
