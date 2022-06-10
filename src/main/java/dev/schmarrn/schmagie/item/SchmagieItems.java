package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.Schmagie;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;

public class SchmagieItems {
	public static final ItemGroup ITEM_GROUP = QuiltItemGroup.builder(new Identifier(Schmagie.MOD_ID, "schmagie")).icon(() -> new ItemStack(Items.OBSIDIAN)).build();

	public static final Item WOODEN_STAFF = new Staff(0, 0, ToolMaterials.WOOD, BlockTags.SHOVEL_MINEABLE, new Item.Settings().group(ITEM_GROUP));

	private static void registerItem(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(Schmagie.MOD_ID, name), item);
	}


	public static void init() {
		registerItem("wooden_staff", WOODEN_STAFF);
	}
}
