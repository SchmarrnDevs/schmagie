package dev.schmarrn.schmagie.common.block;

import dev.schmarrn.schmagie.common.Schmagie;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.loot.JEntry;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.tags.JTag;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.tag.api.QuiltTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static dev.schmarrn.schmagie.common.Schmagie.ITEM_GROUP;
import static net.devtech.arrp.json.loot.JLootTable.entry;
import static net.devtech.arrp.json.models.JModel.condition;

public class Obelisks {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("schmagie:test");

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

	private static void addObeliskTier(Block base) {
		BASE_IDS.add(add(base, Type.BASE));
		IDS.add(add(base, Type.NORMAL));
		TOP_IDS.add(add(base, Type.TOP));
	}

	private static Identifier add(Block base, Type obeliskType) {
		String name = obeliskType + "_" + Registry.BLOCK.getId(base).getPath();
		Identifier id = new Identifier(Schmagie.MOD_ID, name);

		Block block = switch (obeliskType) {
			case NORMAL -> new Obelisk(base);
			case BASE -> new ObeliskBase(base);
			case TOP -> new ObeliskTop(base);
		};

		Registry.register(Registry.BLOCK, id, block);
		Registry.register(Registry.ITEM, id, new BlockItem(block, new Item.Settings().group(ITEM_GROUP)));

		RESOURCE_PACK.addBlockState(JState.state(JState.variant(JState.model("schmagie:block/"+name))), new Identifier(Schmagie.MOD_ID, name));
		RESOURCE_PACK.addModel(JModel.model("schmagie:block/" + obeliskType), new Identifier(Schmagie.MOD_ID, "items/"+name));
		RESOURCE_PACK.addLootTable(
				RuntimeResourcePack.id(Schmagie.MOD_ID, "blocks/" + name),
				JLootTable.loot("minecraft:block")
						.pool(JLootTable.pool()
								.rolls(1)
								.entry(entry().type("minecraft:item").name(id.toString()))
								.condition(condition().condition("minecraft:survives_explosion"))
						)
		);

		JTag pickaxe = tagMap.getOrDefault(new Identifier("minecraft:blocks/mineable/pickaxe"), JTag.tag());
		JTag needs_diamond_tool = tagMap.getOrDefault(new Identifier("minecraft:blocks/needs_diamond_tool"), JTag.tag());

		RESOURCE_PACK.addTag(new Identifier("minecraft:blocks/mineable/pickaxe"), pickaxe.add(id));
		RESOURCE_PACK.addTag(new Identifier("minecraft:blocks/needs_diamond_tool"), needs_diamond_tool.add(id));

		tagMap.put(new Identifier("minecraft:blocks/mineable/pickaxe"), pickaxe);
		tagMap.put(new Identifier("minecraft:blocks/needs_diamond_tool"), needs_diamond_tool);

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
			l.add(Registry.BLOCK.get(id));
		}
		return l;
	}

	public static void init() {
		addObeliskTier(Blocks.DIRT);
		addObeliskTier(Blocks.SANDSTONE);
		addObeliskTier(Blocks.OBSIDIAN);
		addObeliskTier(Blocks.EMERALD_BLOCK);

		RRPCallback.AFTER_VANILLA.register(a -> {
			a.add(RESOURCE_PACK);
		});
	}
}
