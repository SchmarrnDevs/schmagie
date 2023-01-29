package dev.schmarrn.schmagie.client.model;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SimplerModel implements UnbakedModel, BakedModel, FabricBakedModel {
	private static final float PIXEL = 1f / 16f;

	protected TextureAtlasSprite[] sprites;

	abstract Material[] getSpriteIdentifiers();

	private Mesh mesh;

	private static final ResourceLocation DEFAULT_BLOCK_MODEL = new ResourceLocation("minecraft:block/block");

	private ItemTransforms transformation;

	// Unbaked Model

//	@Override
//	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
//		return Arrays.asList(getSpriteIdentifiers());
//	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> models) {

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

	@Nullable
	@Override
	public BakedModel bake(ModelBaker modelBaker, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
		BuiltInModel defaultBlockModel = (BuiltInModel) modelBaker.getModel(DEFAULT_BLOCK_MODEL);
		transformation = defaultBlockModel.getTransforms();

		Material[] ids = getSpriteIdentifiers();
		sprites = new TextureAtlasSprite[ids.length];

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
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource randomGenerator) {
		// Don't need because we use FabricBakedModel instead. However, it's better to not return null in case some mod decides to call this function.
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true; // we want the block to have a shadow depending on the adjacent blocks
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return true;
	}

	@NotNull
	@Override
	public ItemTransforms getTransforms() {
		return transformation;
	}

	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}

	// Fabric Baked Model

	@Override
	public boolean isVanillaAdapter() {
		return false; // False to trigger FabricBakedModel rendering
	}

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		context.meshConsumer().accept(mesh);
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<RandomSource> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(mesh);
	}
}
