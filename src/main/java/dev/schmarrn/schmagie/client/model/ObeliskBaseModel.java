package dev.schmarrn.schmagie.client.model;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Collections;

public class ObeliskBaseModel extends SimplerModel {
	private final SpriteIdentifier base;

	ObeliskBaseModel(String base) {
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
		for (Direction direction : Direction.values()) {
			if (direction == Direction.UP) {
				pixel_square_emit(emitter, direction, 1, 1, 15, 15, 0, 0);

				// Depth 1
				pixel_square_emit(emitter, direction, 0, 16, 1, 0);
			} else if (direction == Direction.DOWN) {
				pixel_square_emit(emitter, direction, 0, 16, 0, 0);
			} else {
				// Upper thing:
				pixel_square_emit(emitter, direction, 1, 15, 15, 16, 1, 0);
				// Lower thing:
				pixel_square_emit(emitter, direction, 0, 0, 16, 15, 0, 0);
			}
		}
	}

	@Override
	public Sprite getParticleSprite() {
		return sprites[0];
	}
}
