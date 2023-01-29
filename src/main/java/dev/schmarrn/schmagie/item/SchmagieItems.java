package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.Schmagie;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class SchmagieItems {
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(Schmagie.MOD_ID, "schmagie")).icon(() -> new ItemStack(Items.OBSIDIAN)).build();

	public static final Item WOODEN_STAFF = new Staff(0, 0, ToolMaterials.WOOD, BlockTags.SHOVEL_MINEABLE, getDefaultSettings());

	static Item.Settings getDefaultSettings() {
		return new QuiltItemSettings();
	}

	private static void registerItem(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier(Schmagie.MOD_ID, name), item);

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			content.addItem(item);
		});
	}


	public static void init() {
		registerItem("wooden_staff", WOODEN_STAFF);
		MagicalPigments.init();
	}
}
