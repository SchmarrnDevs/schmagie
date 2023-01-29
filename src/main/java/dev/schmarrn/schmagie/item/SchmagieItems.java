package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.Schmagie;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class SchmagieItems {
	public static final CreativeModeTab ITEM_GROUP = FabricItemGroup.builder(new ResourceLocation(Schmagie.MOD_ID, "schmagie")).icon(() -> new ItemStack(Items.OBSIDIAN)).build();

	public static final Item WOODEN_STAFF = new Staff(0, 0, Tiers.WOOD, BlockTags.MINEABLE_WITH_SHOVEL, getDefaultSettings());

	static Item.Properties getDefaultSettings() {
		return new QuiltItemSettings();
	}

	private static void registerItem(String name, Item item) {
		Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Schmagie.MOD_ID, name), item);


		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			content.accept(item);
		});
	}


	public static void init() {
		registerItem("wooden_staff", WOODEN_STAFF);
		MagicalPigments.init();
	}
}
