package dev.schmarrn.schmagie.client;

import dev.schmarrn.schmagie.client.model.MyModelProvider;
import dev.schmarrn.schmagie.block.Obelisks;
import dev.schmarrn.schmagie.block.entity.ObeliskEntity;
import dev.schmarrn.schmagie.item.MagicalPigmentItem;
import dev.schmarrn.schmagie.item.MagicalPigments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

@Environment(EnvType.CLIENT)
public class SchmagieClient implements ClientModInitializer {

	@Override
	public void onInitializeClient(ModContainer mod) {
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> {
			return new MyModelProvider();
		});

		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), Obelisks.getObeliskBlocks().toArray(new Block[0]));

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			ObeliskEntity e = (ObeliskEntity) view.getBlockEntity(pos);
			if (e == null) return 0;
			int[] colordata = e.getRenderAttachmentData().getColor();
			if (colordata.length != 4) return 0;
			DyeColor col = DyeColor.byId(e.getRenderAttachmentData().getColor()[tintIndex]);
			return col.getSignColor();
		}, Obelisks.getObeliskBlocks().toArray(new Block[0]));

		ColorProviderRegistry.ITEM.register(
				(stack, index) -> ((MagicalPigmentItem) (stack.getItem())).getColor().getSignColor(),
				MagicalPigments.getMagicalPigmentItems().toArray(new Item[0])
		);
	}

}
