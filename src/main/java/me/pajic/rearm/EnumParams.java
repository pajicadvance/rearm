package me.pajic.rearm;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

@SuppressWarnings("unused")
public class EnumParams {
    public static final EnumProxy<Gui.HeartType> PROXY = new EnumProxy<>(
            Gui.HeartType.class,
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_full"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_half"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_half_blinking"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_hardcore_full"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_hardcore_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_hardcore_half"),
            ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "hud/heart/bleeding_hardcore_half_blinking")
    );
}