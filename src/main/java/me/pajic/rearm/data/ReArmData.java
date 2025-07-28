package me.pajic.rearm.data;

import me.pajic.rearm.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public class ReArmData {

    @SubscribeEvent
    public static void registerDatapacks(AddPackFindersEvent event) {
        if (Main.CONFIG.bow.enableBackstep.get()) event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "resourcepacks/backstep"),
                PackType.SERVER_DATA,
                Component.literal("ReArm Backstep"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
        if (Main.CONFIG.axe.cripplingThrow.get()) event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "resourcepacks/crippling_throw"),
                PackType.SERVER_DATA,
                Component.literal("ReArm Crippling Throw"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
        if (Main.CONFIG.protection.magicProtection.get()) event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "resourcepacks/magic_protection"),
                PackType.SERVER_DATA,
                Component.literal("ReArm Magic Protection"),
                PackSource.BUILT_IN,
                true,
                Pack.Position.TOP
        );
    }
}
