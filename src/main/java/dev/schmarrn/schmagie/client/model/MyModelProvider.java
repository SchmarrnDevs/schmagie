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

	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
		if (identifier.equals(OBELISK_MODEL_BLOCK) || identifier.equals(OBELISK_MODEL_ITEM)) {
			return new ObeliskModel();
		} else if (identifier.equals(OBELISK_TOP_MODEL_BLOCK) || identifier.equals(OBELISK_TOP_MODEL_ITEM)) {
			return new ObeliskTopModel();
		} else {
			return null;
		}
	}
}
