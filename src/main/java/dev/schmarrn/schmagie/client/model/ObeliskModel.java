package dev.schmarrn.schmagie.client.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import dev.schmarrn.schmagie.common.Schmagie;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class ObeliskModel implements UnbakedModel, BakedModel, FabricBakedModel {
	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[]{
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
	private final Sprite[] sprites = new Sprite[9];

	private static final float PIXEL = 1.0f / 16.0f;

	private Mesh mesh;

	private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

	private ModelTransformation transformation;

	// Unbaked Model

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList(); // This model does not depend on other models.
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(SPRITE_IDS);
	}

	private void pixel_square_emit(QuadEmitter emitter, Direction direction, int left, int right, int depth) {
		emitter.square(direction, left * PIXEL, 0.0f, right * PIXEL, 1.0f, depth * PIXEL);
		emitter.spriteBake(0, sprites[0], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}

	private void pixel_square_emit(QuadEmitter emitter, Direction direction, int left, int bottom, int right, int top, int depth) {
		emitter.square(direction, left * PIXEL, bottom * PIXEL, right * PIXEL, top * PIXEL, depth * PIXEL);
		emitter.spriteBake(0, sprites[0], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}

	private void bake(QuadEmitter emitter) {
		for (Direction direction : Direction.values()) {
			if (direction.getAxis().isVertical()) {
				// Render Top / Bottom Face
				pixel_square_emit(emitter, direction, 2, 14, 0);

				pixel_square_emit(emitter, direction, 1, 1, 2, 15, 0);
				pixel_square_emit(emitter, direction, 14, 1, 15, 15, 0);

				pixel_square_emit(emitter, direction, 0, 2, 1, 14, 0);
				pixel_square_emit(emitter, direction, 15, 2, 16, 14, 0);
			} else {
				// Outer Face
				pixel_square_emit(emitter, direction, 2, 14, 0);
				// Inner 2 Side Squares
				pixel_square_emit(emitter, direction, 1, 2, 1);
				pixel_square_emit(emitter, direction, 14, 15, 1);
				// Innerererer 2 Side Squares
				pixel_square_emit(emitter, direction, 0, 1, 2);
				pixel_square_emit(emitter, direction, 15, 16, 2);
			}
		}
	}

	private void emitRunes(QuadEmitter emitter, ObeliskEntity entity) {
		if (entity == null) {
			Schmagie.LOGGER.warn("emitRunes: ObeliskEntity is null");
			return;
		}
		for (Direction direction : Direction.values()) {
			if (!direction.getAxis().isVertical()) {
				int spriteIndex = entity.getRenderAttachmentData().getRune(direction);
				emitter.square(direction, 2.0f / 16.0f, 0.0f, 1.0f - 2.0f / 16.0f, 1.0f, 0.0f);
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
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
		transformation = defaultBlockModel.getTransformations();

		for (int i = 0; i < sprites.length; ++i) {
			sprites[i] = textureGetter.apply(SPRITE_IDS[i]);
		}
		// Build the mesh using the Renderer API
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		this.bake(emitter);

		mesh = builder.build();

		return this;
	}

	// Baked Model
	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		// Don't need because we use FabricBakedModel instead. However, it's better to not return null in case some mod decides to call this function.
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true; // we want the block to have a shadow depending on the adjacent blocks
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public Sprite getParticleSprite() {
		return sprites[0];
	}

	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	// Fabric Baked Model

	@Override
	public boolean isVanillaAdapter() {
		return false; // False to trigger FabricBakedModel rendering
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
		// Render function
		QuadEmitter emitter = renderContext.getEmitter();
		Schmagie.LOGGER.info("emitBlockQuads");
		renderContext.meshConsumer().accept(mesh);
		this.emitRunes(emitter, (ObeliskEntity) blockRenderView.getBlockEntity(blockPos));
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(mesh);
	}
}
