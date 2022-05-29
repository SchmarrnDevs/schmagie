package dev.schmarrn.schmagie.client.model;

import dev.schmarrn.schmagie.common.Schmagie;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MyModelProvider implements ModelResourceProvider {
	public static final Identifier OBELISK_MODEL_BLOCK = new Identifier(Schmagie.MOD_ID, "block/obelisk");
	public static final Identifier OBELISK_MODEL_ITEM = new Identifier(Schmagie.MOD_ID, "item/obelisk");

	public static final Identifier OBELISK_TOP_MODEL_BLOCK = new Identifier(Schmagie.MOD_ID, "block/obelisk_top");
	public static final Identifier OBELISK_TOP_MODEL_ITEM = new Identifier(Schmagie.MOD_ID, "item/obelisk_top");

	public static final Identifier OBELISK_BASE_MODEL_BLOCK = new Identifier(Schmagie.MOD_ID, "block/obelisk_base");
	public static final Identifier OBELISK_BASE_MODEL_ITEM = new Identifier(Schmagie.MOD_ID, "item/obelisk_base");

	private boolean is(Identifier comp, Identifier block, Identifier item) {
		return comp.equals(block) || comp.equals(item);
	}

	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
		if (is(identifier, OBELISK_MODEL_BLOCK, OBELISK_MODEL_ITEM)) {
			return new ObeliskModel("minecraft:block/obsidian");
		} else if (is(identifier, OBELISK_TOP_MODEL_BLOCK, OBELISK_TOP_MODEL_ITEM)) {
			return new ObeliskTopModel("minecraft:block/obsidian");
		} else if (is(identifier, OBELISK_BASE_MODEL_BLOCK, OBELISK_BASE_MODEL_ITEM)) {
			return new ObeliskBaseModel("minecraft:block/obsidian");
		} else {
			return null;
		}
	}
}
