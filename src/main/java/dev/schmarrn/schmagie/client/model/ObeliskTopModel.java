package dev.schmarrn.schmagie.client.model;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Collections;

public class ObeliskTopModel extends SimplerModel {
	private final SpriteIdentifier base;

	ObeliskTopModel(String base) {
		super();
		this.base = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(base));
	}
	@Override
	SpriteIdentifier[] getSpriteIdentifiers() {
		return new SpriteIdentifier[]{
				base
		};
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Override
	void bake(QuadEmitter emitter) {
		for (Direction dir : Direction.values()) {
			switch (dir) {
				case UP:
					// Top face
					pixel_square_emit(emitter, dir, 5, 4, 11, 12, 8, 0);
					pixel_square_emit(emitter, dir, 4, 5, 5, 11, 8, 0);
					pixel_square_emit(emitter, dir, 11, 5, 12, 11, 8, 0);

					for (int depth = 3; depth != 0; depth--) {
						// South
						pixel_square_emit(emitter, dir, depth + 1, depth, 15 - depth, depth + 1, 16 - depth * 2, 0);
						// North
						pixel_square_emit(emitter, dir, depth + 1, 15 - depth, 15 - depth, 16 - depth, 16 - depth * 2, 0);
						// West
						pixel_square_emit(emitter, dir, depth, depth + 1, depth + 1, 15 - depth, 16 - depth * 2, 0);
						// East
						pixel_square_emit(emitter, dir, 15 - depth, depth + 1, 16 - depth, 15 - depth, 16 - depth * 2, 0);

						// Corners:
						// West-South
						pixel_square_emit(emitter, dir, depth + 1, depth + 1, depth + 2, depth + 2, 16 - depth * 2, 0);
						// West-North
						pixel_square_emit(emitter, dir, depth + 1, 14 - depth, depth + 2, 15 - depth, 16 - depth * 2, 0);
						// East-South
						pixel_square_emit(emitter, dir, 14 - depth, depth + 1, 15 - depth, depth + 2, 16 - depth * 2, 0);
						// East-North
						pixel_square_emit(emitter, dir, 14 - depth, 14 - depth, 15 - depth, 15 - depth, 16 - depth * 2, 0);

					}
					break;
				case DOWN:
					pixel_square_emit(emitter, dir, 2, 1, 14, 15, 0, 0);

					pixel_square_emit(emitter, dir, 1, 2, 2, 14, 0, 0);
					pixel_square_emit(emitter, dir, 14, 2, 15, 14, 0, 0);
					break;
				default:
					// Sides
					for (int i = 0; i < 4; ++i) {
						// The long bar
						pixel_square_emit(emitter, dir, i + 2, 2 * i, 14 - i, 2 * i + 2, i + 1, 0);
						// The two short pixels:
						pixel_square_emit(emitter, dir, i + 1, 2 * i, i + 2, 2 * i + 2, i + 2, 0);
						pixel_square_emit(emitter, dir, 14 - i, 2 * i, 15 - i, 2 * i + 2, i + 2, 0);
					}
			}
		}
	}

	@Override
	public Sprite getParticleSprite() {
		return sprites[0];
	}
}
