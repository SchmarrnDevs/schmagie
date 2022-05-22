package dev.schmarrn.schmagie.client.model;

import dev.schmarrn.schmagie.common.Schmagie;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MyModelProvider implements ModelResourceProvider {
    public static final Identifier OBELISK_MODEL_BLOCK = new Identifier(Schmagie.MOD_ID, "block/obelisk");
    public static final Identifier OBELISK_MODEL_ITEM  = new Identifier(Schmagie.MOD_ID, "item/obelisk");
    @Override
    public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
        if(identifier.equals(OBELISK_MODEL_BLOCK) || identifier.equals(OBELISK_MODEL_ITEM)) {
            Schmagie.LOGGER.info("Load Obsidian Model");
            return new ObeliskModel();
        } else {
            return null;
        }
    }
}
