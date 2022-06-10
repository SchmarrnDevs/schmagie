package dev.schmarrn.schmagie.client.model;

import dev.schmarrn.schmagie.block.Obelisks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MyModelProvider implements ModelResourceProvider {
	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) {
		if (Obelisks.isObelisk(identifier)) {
			String material = identifier.getPath().split(Obelisks.Type.NORMAL + "_")[1];
			return new ObeliskModel("minecraft:block/" + material);
		} else if (Obelisks.isObeliskBase(identifier)) {
			String material = identifier.getPath().split(Obelisks.Type.BASE + "_")[1];
			return new ObeliskBaseModel("minecraft:block/" + material);
		} else if (Obelisks.isObeliskTop(identifier)) {
			String material = identifier.getPath().split(Obelisks.Type.TOP + "_")[1];
			return new ObeliskTopModel("minecraft:block/" + material);
		} else {
			return null;
		}
	}
}
