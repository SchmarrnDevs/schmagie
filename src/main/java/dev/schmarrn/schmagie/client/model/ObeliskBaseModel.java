package dev.schmarrn.schmagie.client.model;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Collection;
import java.util.Collections;

public class ObeliskBaseModel extends SimplerModel {
	private final Material base;

	ObeliskBaseModel(String base) {
		super();
		this.base = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(base));
	}


	@Override
	Material[] getSpriteIdentifiers() {
		return new Material[]{
				base
		};
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
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
	public TextureAtlasSprite getParticleIcon() {
		return sprites[0];
	}
}
