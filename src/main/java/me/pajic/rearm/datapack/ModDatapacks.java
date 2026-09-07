package me.pajic.rearm.datapack;

import me.pajic.rearm.ReArm;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ModDatapacks {

    private static final Set<String> PACKS = new HashSet<>();

    public static void init() {
        if (ReArm.CONFIG.bow.enableBackstep.get()) PACKS.add("backstep");
        if (ReArm.CONFIG.shield.enableBash.get()) PACKS.add("bash");
        if (ReArm.CONFIG.axe.cripplingThrow.get()) PACKS.add("crippling_throw");
        if (ReArm.CONFIG.protection.magicProtection.get()) PACKS.add("magic_protection");
    }

    public static Stream<String> getPacks() {
        return PACKS.stream();
    }
}
