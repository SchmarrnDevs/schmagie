package dev.schmarrn.schmagie.datagen;

import dev.schmarrn.schmagie.block.Obelisks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.BiConsumer;

public class SchmagieLootTables extends SimpleFabricLootTableProvider {
	public SchmagieLootTables(FabricDataOutput dataOutput) {
		super(dataOutput, LootContextParamSets.BLOCK);
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> identifierBuilderBiConsumer) {
		for (ResourceLocation id : Obelisks.getIds()) {
			//identifierBuilderBiConsumer.accept(id, );
			identifierBuilderBiConsumer.accept(id, LootTable.lootTable());
		}
	}
}
