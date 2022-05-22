package dev.schmarrn.schmagie.client;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import dev.schmarrn.schmagie.client.model.MyModelProvider;
import dev.schmarrn.schmagie.common.Schmagie;
import dev.schmarrn.schmagie.common.block.Obelisk;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.DyeColor;

@Environment(EnvType.CLIENT)
public class SchmagieClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> {
            return new MyModelProvider();
        });

        BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), Schmagie.OBELISK);

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            ObeliskEntity e = (ObeliskEntity) view.getBlockEntity(pos);
            if (e == null) return 0;
			int[] colordata = e.getRenderAttachmentData();
			Schmagie.LOGGER.info("colordata length: {}, tintIndex: {}, pos: {}", colordata.length, tintIndex, pos);
			if (colordata.length != 4) return 0;
            DyeColor col = DyeColor.byId(e.getRenderAttachmentData()[tintIndex]);
            return col.getSignColor();
		}, Schmagie.OBELISK);
    }

}
