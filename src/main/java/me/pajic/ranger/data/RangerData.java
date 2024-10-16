package me.pajic.ranger.data;

import me.pajic.ranger.Main;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

public class RangerData {

    public static void initData() {
        FabricLoader.getInstance().getModContainer("ranger").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    ResourceLocation.parse("ranger:fixmc277617"),
                    modContainer,
                    ResourcePackActivationType.ALWAYS_ENABLED
            );

            ResourceManagerHelper.registerBuiltinResourcePack(
                    ResourceLocation.parse("ranger:enchantability"),
                    modContainer,
                    ResourcePackActivationType.ALWAYS_ENABLED
            );

            if (Main.CONFIG.bow.enableBackstep()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("ranger:backstep"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }

            if (Main.CONFIG.other.infinimending()) {
                ResourceManagerHelper.registerBuiltinResourcePack(
                        ResourceLocation.parse("ranger:infinimending"),
                        modContainer,
                        ResourcePackActivationType.ALWAYS_ENABLED
                );
            }
        });
    }
}
