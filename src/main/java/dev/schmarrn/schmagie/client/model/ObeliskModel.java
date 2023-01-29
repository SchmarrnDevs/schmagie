package dev.schmarrn.schmagie.client.model;

import dev.schmarrn.schmagie.Schmagie;
import dev.schmarrn.schmagie.block.entity.ObeliskEntity;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class ObeliskModel extends SimplerModel {
	private final Material base;

	ObeliskModel(String base) {
		super();
		this.base = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(base));
	}

	@Override
	Material[] getSpriteIdentifiers() {
		return new Material[]{
				base,
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune0")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune1")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune2")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune3")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune4")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune5")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune6")),
				new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Schmagie.MOD_ID, "block/rune7"))
		};
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptyList();
	}

	@Override
	void bake(QuadEmitter emitter) {
		for (Direction direction : Direction.values()) {
			if (direction.getAxis().isVertical()) {
				// Render Top / Bottom Face
				pixel_square_emit(emitter, direction, 2, 1, 14, 15, 0, 0);

				pixel_square_emit(emitter, direction, 1, 2, 2, 14, 0, 0);
				pixel_square_emit(emitter, direction, 14, 2, 15, 14, 0, 0);
			} else {
				// Outer Face
				pixel_square_emit(emitter, direction, 2, 14, 1, 0);
				// Inner 2 Side Rectangles
				pixel_square_emit(emitter, direction, 1, 2, 2, 0);
				pixel_square_emit(emitter, direction, 14, 15, 2, 0);
			}
		}
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return sprites[0];
	}

	private void emitRunes(QuadEmitter emitter, ObeliskEntity entity) {
		if (entity == null) {
			Schmagie.LOGGER.warn("emitRunes: ObeliskEntity is null");
			return;
		}
		for (Direction direction : Direction.values()) {
			if (!direction.getAxis().isVertical()) {
				int spriteIndex = entity.getRenderAttachmentData().getRune(direction);
				if (spriteIndex < 0) {
					continue;
				}
				emitter.square(direction, 2.0f / 16.0f, 0.0f, 1.0f - 2.0f / 16.0f, 1.0f, 1f / 16f);
				emitter.spriteBake(0, sprites[spriteIndex + 1], MutableQuadView.BAKE_LOCK_UV);
				emitter.material(IndigoRenderer.INSTANCE.materialFinder().emissive(spriteIndex+1, true).find());
				// Enable texture usage
				emitter.spriteColor(0, -1, -1, -1, -1);
				// Assign each direction their own texture index
				emitter.colorIndex(direction.get2DDataValue());
				emitter.emit();
			}
		}
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		// Render the baked model
		super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
		// Render the runes on top of it
		QuadEmitter emitter = context.getEmitter();
		this.emitRunes(emitter, (ObeliskEntity) blockView.getBlockEntity(pos));
	}
}
