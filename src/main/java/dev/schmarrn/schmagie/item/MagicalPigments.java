package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.Schmagie;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class MagicalPigments {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("schmagie:pigment");

	private static final List<Identifier> MAGICAL_PIGMENTS = new ArrayList<>();

	private static void add(DyeColor color) {
		String name = color.name().toLowerCase() + "_magical_pigment";
		Identifier id = new Identifier(Schmagie.MOD_ID, name);

		Item item = new MagicalPigmentItem(SchmagieItems.getDefaultSettings(), color);

		Registry.register(Registry.ITEM, id, item);

		RESOURCE_PACK.addModel(
				JModel.model("item/generated")
						.textures(JModel.textures()
								.layer0("schmagie:item/magical_dye_grayscale"))
				, new Identifier(Schmagie.MOD_ID, "item/" + name));
		MAGICAL_PIGMENTS.add(id);

	}

	public static List<Item> getMagicalPigmentItems() {
		List<Item> l = new ArrayList<>();
		for (var id : MAGICAL_PIGMENTS) {
			l.add(Registry.ITEM.get(id));
		}
		return l;
	}

	public static void init() {
		add(DyeColor.RED);
		add(DyeColor.BLUE);
		add(DyeColor.YELLOW);
		add(DyeColor.GREEN);
		add(DyeColor.ORANGE);
		add(DyeColor.PURPLE);

		RRPCallback.AFTER_VANILLA.register(a -> a.add(RESOURCE_PACK));
	}
}
