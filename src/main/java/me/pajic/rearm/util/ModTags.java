package me.pajic.rearm.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
	public static final TagKey<Item> SHIELDS = TagKey.create(
			Registries.ITEM,
			Identifier.fromNamespaceAndPath("c", "tools/shield")
	);
}
