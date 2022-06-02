package dev.schmarrn.schmagie.common;

import dev.schmarrn.schmagie.common.block.ObeliskBase;
import dev.schmarrn.schmagie.common.block.Obelisks;
import net.minecraft.item.*;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.schmarrn.schmagie.common.block.Obelisk;
import dev.schmarrn.schmagie.common.block.ObeliskTop;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import dev.schmarrn.schmagie.common.item.Staff;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Schmagie implements ModInitializer {
	public static final String MOD_ID = "schmagie";
	public static final String MOD_NAME = "Schmagie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);


	public static final ItemGroup ITEM_GROUP = QuiltItemGroup.builder(new Identifier(MOD_ID, "schmagie")).icon(() -> new ItemStack(Items.OBSIDIAN)).build();

	public static final Item WOODEN_STAFF = new Staff(0, 0, ToolMaterials.WOOD, BlockTags.SHOVEL_MINEABLE, new Item.Settings().group(ITEM_GROUP));

//	public static final Block OBELISK = new Obelisk(AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(50.0F, 1200.0F));
	public static BlockEntityType<ObeliskEntity> OBELISK_BLOCK_ENTITY;
//	public static final Block OBELISK_TOP = new ObeliskTop(AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(50.0F, 1200.0F));

//	public static final Block OBELISK_BASE = new ObeliskBase(AbstractBlock.Settings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(50.0f, 1200.0f));

	private void registerItem(String name, Item item) {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), item);
	}

	private void registerBlock(String name, Block block) {
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
		registerItem(name, new BlockItem(block, new Item.Settings().group(ITEM_GROUP)));
	}

	@Override
	public void onInitialize(ModContainer mod) {
		registerItem("wooden_staff", WOODEN_STAFF);

		Obelisks.init();

		OBELISK_BLOCK_ENTITY = Registry.register(
				Registry.BLOCK_ENTITY_TYPE,
				new Identifier(MOD_ID, "obelisk_block_entity"),
				FabricBlockEntityTypeBuilder.create(
						ObeliskEntity::new,
						Obelisks.getObeliskBlocks().toArray(new Block[0])
						).build(null));
	}
}
