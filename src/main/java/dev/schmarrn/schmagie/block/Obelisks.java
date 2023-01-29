package dev.schmarrn.schmagie.block;

import dev.schmarrn.schmagie.Schmagie;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.schmarrn.schmagie.item.SchmagieItems.ITEM_GROUP;
import static net.devtech.arrp.json.loot.JLootTable.entry;
import static net.devtech.arrp.json.models.JModel.condition;

public class Obelisks {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("schmagie:obelisk");

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

	private static final HashMap<Identifier, JTag> tagMap = new HashMap<>();

	private static final List<Identifier> IDS = new ArrayList<>();
	private static final List<Identifier> BASE_IDS = new ArrayList<>();
	private static final List<Identifier> TOP_IDS = new ArrayList<>();

	private static void addObeliskTier(Block base, EffectiveTool tool, int miningLevel) {
		BASE_IDS.add(add(base, tool, miningLevel, Type.BASE));
		IDS.add(add(base, tool, miningLevel, Type.NORMAL));
		TOP_IDS.add(add(base, tool, miningLevel, Type.TOP));
	}

	private static Identifier add(Block base, EffectiveTool tool, int miningLevel, Type obeliskType) {
		String name = obeliskType + "_" + Registries.BLOCK.getId(base).getPath();
		Identifier id = new Identifier(Schmagie.MOD_ID, name);

		Block block = switch (obeliskType) {
			case NORMAL -> new Obelisk(base);
			case BASE -> new ObeliskBase(base);
			case TOP -> new ObeliskTop(base);
		};

		Registry.register(Registries.BLOCK, id, block);
		BlockItem item = new BlockItem(block, new Item.Settings());
		Registry.register(Registries.ITEM, id, item);

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> content.addItem(item));

		RESOURCE_PACK.addBlockState(JState.state(JState.variant(JState.model("schmagie:block/"+name))), new Identifier(Schmagie.MOD_ID, name));
		RESOURCE_PACK.addModel(JModel.model("schmagie:block/" + obeliskType), new Identifier(Schmagie.MOD_ID, "item/"+name));
		RESOURCE_PACK.addLootTable(
				RuntimeResourcePack.id(Schmagie.MOD_ID, "blocks/" + name),
				JLootTable.loot("minecraft:block")
						.pool(JLootTable.pool()
								.rolls(1)
								.entry(entry().type("minecraft:item").name(id.toString()))
								.condition(condition().condition("minecraft:survives_explosion"))
						)
		);
		if (miningLevel > 0) {
			TagKey<Block> tmp = MiningLevelManager.getBlockTag(miningLevel);
			Identifier toolID = new Identifier(tmp.id().getNamespace(), "blocks/" + tmp.id().getPath());
			Schmagie.LOGGER.info("{}", toolID);
			JTag needs_diamond_tool = tagMap.getOrDefault(toolID, JTag.tag());
			RESOURCE_PACK.addTag(toolID, needs_diamond_tool.add(id));
			tagMap.put(toolID, needs_diamond_tool);
		}

		JTag pickaxe = tagMap.getOrDefault(new Identifier("minecraft:blocks/mineable/" + tool), JTag.tag());
		RESOURCE_PACK.addTag(new Identifier("minecraft:blocks/mineable/" + tool), pickaxe.add(id));
		tagMap.put(new Identifier("minecraft:blocks/mineable/" + tool), pickaxe);

		return id;
	}

	private static boolean is(Identifier name, List<Identifier> ids) {
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

	public static boolean isObelisk(Identifier name) {
		return is(name, IDS);
	}

	public static boolean isObeliskBase(Identifier name) {
		return is(name, BASE_IDS);
	}

	public static boolean isObeliskTop(Identifier name) {
		return is(name, TOP_IDS);
	}

	public static List<Block> getObeliskBlocks() {
		List<Block> l = new ArrayList<>();
		for (var id : IDS) {
			l.add(Registries.BLOCK.get(id));
		}
		return l;
	}

	static void init() {
		addObeliskTier(Blocks.DIRT, EffectiveTool.SHOVEL, 0);
		addObeliskTier(Blocks.SANDSTONE, EffectiveTool.PICKAXE, 0);
		addObeliskTier(Blocks.OBSIDIAN, EffectiveTool.PICKAXE, 3);
		addObeliskTier(Blocks.EMERALD_BLOCK, EffectiveTool.PICKAXE, 2);

		RRPCallback.AFTER_VANILLA.register(a -> a.add(RESOURCE_PACK));
	}
}
