package me.pajic.rearm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class EarlyRiser implements Runnable {

    private static final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

    private static final String targetType = // Gui$HeartType
            mappingResolver.mapClassName("intermediary", "net.minecraft.class_329$class_6411");

    private static final String paramType = // ResourceLocation
            "L" + mappingResolver.mapClassName("intermediary", "net.minecraft.class_2960") + ";";

    @Override
    public void run() {
        ClassTinkerers.enumBuilder(targetType, paramType, paramType, paramType, paramType, paramType, paramType, paramType, paramType)
                .addEnum("BLEEDING", () -> {
                    Supplier<Supplier<Object[]>> supplier = (() -> () -> new Object[] {
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_full"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_full_blinking"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_half"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_half_blinking"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_hardcore_full"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_hardcore_full_blinking"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_hardcore_half"),
                            ResourceLocation.fromNamespaceAndPath("rearm", "hud/heart/bleeding_hardcore_half_blinking")
                    });
                    return supplier.get().get();
                }).build();
    }
}
