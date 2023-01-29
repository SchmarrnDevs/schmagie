package dev.schmarrn.schmagie.block.entity;

import dev.schmarrn.schmagie.Schmagie;
import dev.schmarrn.schmagie.block.Obelisks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SchmagieBlockEntities {
	public static BlockEntityType<ObeliskEntity> OBELISK_BLOCK_ENTITY;

	public static void init() {
		OBELISK_BLOCK_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				new ResourceLocation(Schmagie.MOD_ID, "obelisk_block_entity"),
				FabricBlockEntityTypeBuilder.create(
						ObeliskEntity::new,
						Obelisks.getNormalObelisks().toArray(new Block[0])
				).build(null));
	}
}
