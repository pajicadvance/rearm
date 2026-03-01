package me.pajic.rearm.platform.fabric;

//? fabric {

import com.chocohead.mm.api.ClassTinkerers;
import me.pajic.rearm.ReArm;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class EarlyRiser implements Runnable {

    private static final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

    private static final String targetType = // Gui$HeartType
            mappingResolver.mapClassName("intermediary", "net.minecraft.class_329$class_6411");

    private static final String paramType = // Identifier
            "L" + mappingResolver.mapClassName("intermediary", "net.minecraft.class_2960") + ";";

    @Override
    public void run() {
        ClassTinkerers.enumBuilder(targetType, paramType, paramType, paramType, paramType, paramType, paramType, paramType, paramType)
                .addEnum("REARM_BLEEDING", () -> {
                    Supplier<Supplier<Object[]>> supplier = (() -> () -> new Object[] {
							ReArm.id("hud/heart/bleeding_full"),
							ReArm.id("hud/heart/bleeding_full_blinking"),
							ReArm.id("hud/heart/bleeding_half"),
							ReArm.id("hud/heart/bleeding_half_blinking"),
							ReArm.id("hud/heart/bleeding_hardcore_full"),
							ReArm.id("hud/heart/bleeding_hardcore_full_blinking"),
                            ReArm.id("hud/heart/bleeding_hardcore_half"),
							ReArm.id("hud/heart/bleeding_hardcore_half_blinking")
                    });
                    return supplier.get().get();
                }).build();
    }
}
//?}
