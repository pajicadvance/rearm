package me.pajic.rearm.data;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class ReArmData {

    public static void init() {
        FabricLoader.getInstance().getModContainer("rearm").ifPresent(modContainer -> {

            if (Main.CONFIG.bow.enableBackstep()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:backstep"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }

            if (Main.CONFIG.axe.cripplingThrow()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:crippling_throw"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }

            if (Main.CONFIG.magicProtection()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:magic_protection"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }
        });
    }
}
