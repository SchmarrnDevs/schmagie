package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.Schmagie;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.schmarrn.schmagie.item.SchmagieItems.ITEM_GROUP;

public class MagicalPigments {
	private static final Map<DyeColor, Item> MAGICAL_PIGMENTS = new HashMap<>();

	private static void add(DyeColor color) {
		String name = color.name().toLowerCase() + "_magical_pigment";
		ResourceLocation id = new ResourceLocation(Schmagie.MOD_ID, name);

		Item item = new MagicalPigmentItem(SchmagieItems.getDefaultSettings(), color);

		Registry.register(BuiltInRegistries.ITEM, id, item);

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> content.accept(item));

		MAGICAL_PIGMENTS.put(color, item);
	}

	public static List<Item> getMagicalPigmentItems() {
		return MAGICAL_PIGMENTS.values().stream().toList();
	}

	public static void init() {
		add(DyeColor.RED);
		add(DyeColor.BLUE);
		add(DyeColor.YELLOW);
		add(DyeColor.GREEN);
		add(DyeColor.ORANGE);
		add(DyeColor.PURPLE);
	}
}
