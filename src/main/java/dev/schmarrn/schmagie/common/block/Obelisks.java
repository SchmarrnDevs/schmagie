package dev.schmarrn.schmagie.common.block;

import dev.schmarrn.schmagie.common.Schmagie;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

import static dev.schmarrn.schmagie.common.Schmagie.ITEM_GROUP;

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

		RRPCallback.BEFORE_VANILLA.register(a -> a.add(RESOURCE_PACK));
	}
}
