package dev.schmarrn.schmagie.block;

import dev.schmarrn.schmagie.Schmagie;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static dev.schmarrn.schmagie.item.SchmagieItems.ITEM_GROUP;

public class Obelisks {
	public enum EffectiveTool {
		SHOVEL,
		PICKAXE;

		@Override
		public String toString() {
			return switch(this) {
				case SHOVEL -> "shovel";
				case PICKAXE -> "pickaxe";
			};
		}
	}

	public enum Type {
		NORMAL,
		BASE,
		TOP;

		@Override
		public String toString() {
			return switch (this) {
				case NORMAL -> "obelisk";
				case BASE -> "obelisk_base";
				case TOP -> "obelisk_top";
			};
		}
	}

	private static final HashMap<ResourceLocation, JTag> tagMap = new HashMap<>();

	private static final List<ResourceLocation> IDS = new ArrayList<>();
	private static final List<ResourceLocation> BASE_IDS = new ArrayList<>();
	private static final List<ResourceLocation> TOP_IDS = new ArrayList<>();

	private static void addObeliskTier(Block base, EffectiveTool tool, int miningLevel) {
		BASE_IDS.add(add(base, tool, miningLevel, Type.BASE));
		IDS.add(add(base, tool, miningLevel, Type.NORMAL));
		TOP_IDS.add(add(base, tool, miningLevel, Type.TOP));
	}

	private static ResourceLocation add(Block base, EffectiveTool tool, int miningLevel, Type obeliskType) {
		String name = obeliskType + "_" + BuiltInRegistries.BLOCK.getKey(base).getPath();
		ResourceLocation id = new ResourceLocation(Schmagie.MOD_ID, name);

		Block block = switch (obeliskType) {
			case NORMAL -> new Obelisk(base);
			case BASE -> new ObeliskBase(base);
			case TOP -> new ObeliskTop(base);
		};

		Registry.register(BuiltInRegistries.BLOCK, id, block);
		BlockItem item = new BlockItem(block, new Item.Properties());
		Registry.register(BuiltInRegistries.ITEM, id, item);

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> content.accept(item));

		//RESOURCE_PACK.addBlockState(JState.state(JState.variant(JState.model("schmagie:block/"+name))), new Identifier(Schmagie.MOD_ID, name));
		//RESOURCE_PACK.addModel(JModel.model("schmagie:block/" + obeliskType), new Identifier(Schmagie.MOD_ID, "item/"+name));
//		RESOURCE_PACK.addLootTable(
//				RuntimeResourcePack.id(Schmagie.MOD_ID, "blocks/" + name),
//				JLootTable.loot("minecraft:block")
//						.pool(JLootTable.pool()
//								.rolls(1)
//								.entry(entry().type("minecraft:item").name(id.toString()))
//								.condition(condition().condition("minecraft:survives_explosion"))
//						)
//		);
		if (miningLevel > 0) {
			TagKey<Block> tmp = MiningLevelManager.getBlockTag(miningLevel);
			ResourceLocation toolID = new ResourceLocation(tmp.location().getNamespace(), "blocks/" + tmp.location().getPath());
			Schmagie.LOGGER.info("{}", toolID);
			JTag needs_diamond_tool = tagMap.getOrDefault(toolID, JTag.tag());
			//RESOURCE_PACK.addTag(toolID, needs_diamond_tool.add(id));
			tagMap.put(toolID, needs_diamond_tool);
		}

		JTag pickaxe = tagMap.getOrDefault(new ResourceLocation("minecraft:blocks/mineable/" + tool), JTag.tag());
		//RESOURCE_PACK.addTag(new Identifier("minecraft:blocks/mineable/" + tool), pickaxe.add(id));
		tagMap.put(new ResourceLocation("minecraft:blocks/mineable/" + tool), pickaxe);

		return id;
	}

	private static boolean is(ResourceLocation name, List<ResourceLocation> ids) {
		if (!name.getNamespace().equals(Schmagie.MOD_ID)) {
			return false;
		}
		for (var id : ids) {
			String path = name.getPath().split("/")[1];
			if (path.equals(id.getPath())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isObelisk(ResourceLocation name) {
		return is(name, IDS);
	}

	public static boolean isObeliskBase(ResourceLocation name) {
		return is(name, BASE_IDS);
	}

	public static boolean isObeliskTop(ResourceLocation name) {
		return is(name, TOP_IDS);
	}

	public static List<Block> getNormalObelisks() {
		List<Block> l = new ArrayList<>();
		for (var id : IDS) {
			l.add(BuiltInRegistries.BLOCK.get(id));
		}
		return l;
	}

	public static List<Block> getAllObelisks() {
		List<Block> l = new ArrayList<>();
		for (var id : IDS) {
			l.add(BuiltInRegistries.BLOCK.get(id));
		}
		for (var id : BASE_IDS) {
			l.add(BuiltInRegistries.BLOCK.get(id));
		}
		for (var id : TOP_IDS) {
			l.add(BuiltInRegistries.BLOCK.get(id));
		}
		return l;
	}

	public static List<ResourceLocation> getIds() {
		List<ResourceLocation> id = new ArrayList<>();
		id.addAll(IDS);
		id.addAll(BASE_IDS);
		id.addAll(TOP_IDS);
		return id;
	}

	static void init() {
		addObeliskTier(Blocks.DIRT, EffectiveTool.SHOVEL, 0);
		addObeliskTier(Blocks.SANDSTONE, EffectiveTool.PICKAXE, 0);
		addObeliskTier(Blocks.OBSIDIAN, EffectiveTool.PICKAXE, 3);
		addObeliskTier(Blocks.EMERALD_BLOCK, EffectiveTool.PICKAXE, 2);
	}
}
