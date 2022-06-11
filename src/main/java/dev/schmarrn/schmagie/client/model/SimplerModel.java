package dev.schmarrn.schmagie.client.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SimplerModel implements UnbakedModel, BakedModel, FabricBakedModel {
	private static final float PIXEL = 1f / 16f;

	protected Sprite[] sprites;

	abstract SpriteIdentifier[] getSpriteIdentifiers();

	private Mesh mesh;

	private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

	private ModelTransformation transformation;

	// Unbaked Model

	@Override
	public abstract Collection<Identifier> getModelDependencies();

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(getSpriteIdentifiers());
	}

	protected void pixel_square_emit(QuadEmitter emitter, Direction direction, int left, int right, int depth, int spriteIndex) {
		emitter.square(direction, left * PIXEL, 0.0f, right * PIXEL, 1.0f, depth * PIXEL);
		emitter.spriteBake(0, sprites[spriteIndex], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}

	protected void pixel_square_emit(QuadEmitter emitter, Direction direction, int left, int bottom, int right, int top, int depth, int spriteIndex) {
		emitter.square(direction, left * PIXEL, bottom * PIXEL, right * PIXEL, top * PIXEL, depth * PIXEL);
		emitter.spriteBake(0, sprites[spriteIndex], MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}

	abstract void bake(QuadEmitter emitter);

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
		transformation = defaultBlockModel.getTransformations();

		SpriteIdentifier[] ids = getSpriteIdentifiers();
		sprites = new Sprite[ids.length];

		for (int i = 0; i < sprites.length; ++i) {
			sprites[i] = textureGetter.apply(ids[i]);
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
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomGenerator randomGenerator) {
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
	public abstract Sprite getParticleSprite();

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
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<RandomGenerator> randomSupplier, RenderContext context) {
		context.meshConsumer().accept(mesh);
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<RandomGenerator> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(mesh);
	}
}
