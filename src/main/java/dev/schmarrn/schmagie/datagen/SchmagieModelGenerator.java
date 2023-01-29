package dev.schmarrn.schmagie.datagen;

import dev.schmarrn.schmagie.block.Obelisks;
import dev.schmarrn.schmagie.item.MagicalPigments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;


public class SchmagieModelGenerator extends FabricModelProvider {
	public SchmagieModelGenerator(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
		Obelisks.getAllObelisks().forEach(blockStateModelGenerator::createSimpleFlatItemModel);
	}

	@Override
	public void generateItemModels(ItemModelGenerators itemModelGenerator) {
		MagicalPigments.getMagicalPigmentItems().forEach(item -> {
			ModelTemplate pigmentModel = ModelTemplates.FLAT_ITEM;
			pigmentModel.create(
					ModelLocationUtils.getModelLocation(item),
					TextureMapping.layer0(new ResourceLocation("schmagie:item/magical_dye_grayscale")),
					itemModelGenerator.output
			);
		});
	}
}
