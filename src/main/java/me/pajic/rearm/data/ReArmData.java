package me.pajic.rearm.data;

import me.pajic.rearm.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class ReArmData {

    public static void initData() {
        FabricLoader.getInstance().getModContainer("rearm").ifPresent(modContainer -> {

            if (Main.CONFIG.bow.enableBackstep()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:backstep"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }

            if (Main.CONFIG.cripplingBlow.cripplingBlowAbility()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:crippling_blow"),
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

            if (Main.CONFIG.meleeProtection()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:melee_protection"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }

            if (Main.CONFIG.infinimending()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("rearm:infinimending"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }
        });
    }
}
