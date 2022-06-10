package dev.schmarrn.schmagie.block.entity;

import dev.schmarrn.schmagie.Schmagie;
import dev.schmarrn.schmagie.block.Obelisks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SchmagieBlockEntities {
	public static BlockEntityType<ObeliskEntity> OBELISK_BLOCK_ENTITY;

	public static void init() {
		OBELISK_BLOCK_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE,
				new Identifier(Schmagie.MOD_ID, "obelisk_block_entity"),
				FabricBlockEntityTypeBuilder.create(
						ObeliskEntity::new,
						Obelisks.getObeliskBlocks().toArray(new Block[0])
				).build(null));
	}
}
