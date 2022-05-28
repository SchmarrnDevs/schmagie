package dev.schmarrn.schmagie.client.model;

import dev.schmarrn.schmagie.common.Schmagie;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.Supplier;

public class ObeliskModel extends SimplerModel {
	@Override
	SpriteIdentifier[] getSpriteIdentifiers() {
		return new SpriteIdentifier[]{
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("minecraft:block/obsidian")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune0")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune1")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune2")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune3")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune4")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune5")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune6")),
				new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Schmagie.MOD_ID, "block/rune7"))
		};
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
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
	public Sprite getParticleSprite() {
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
				emitter.square(direction, 2.0f / 16.0f, 0.0f, 1.0f - 2.0f / 16.0f, 1.0f, 1f / 16f);
				emitter.spriteBake(0, sprites[spriteIndex + 1], MutableQuadView.BAKE_LOCK_UV);
				// Enable texture usage
				emitter.spriteColor(0, -1, -1, -1, -1);
				// Assign each direction their own texture index
				emitter.colorIndex(direction.getId() - 2);
				emitter.emit();
			}
		}
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
		// Render the baked model
		super.emitBlockQuads(blockRenderView, blockState, blockPos, supplier, renderContext);
		// Render the runes on top of it
		QuadEmitter emitter = renderContext.getEmitter();
		this.emitRunes(emitter, (ObeliskEntity) blockRenderView.getBlockEntity(blockPos));
	}
}
