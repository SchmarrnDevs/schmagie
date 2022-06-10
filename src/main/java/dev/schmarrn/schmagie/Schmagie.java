package dev.schmarrn.schmagie;

import dev.schmarrn.schmagie.block.SchmagieBlocks;
import dev.schmarrn.schmagie.block.entity.SchmagieBlockEntities;
import dev.schmarrn.schmagie.item.SchmagieItems;
import dev.schmarrn.schmagie.item.Staff;
import dev.schmarrn.schmagie.world.biom.SchmagieBiomes;
import dev.schmarrn.schmagie.world.dimension.NetworkDimension;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schmagie implements ModInitializer {
	public static final String MOD_ID = "schmagie";
	public static final String MOD_NAME = "Schmagie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);




	@Override
	public void onInitialize(ModContainer mod) {
		SchmagieItems.init();
		SchmagieBlocks.init();
		SchmagieBlockEntities.init();
		SchmagieBiomes.init();
		NetworkDimension.init();

	}
}
