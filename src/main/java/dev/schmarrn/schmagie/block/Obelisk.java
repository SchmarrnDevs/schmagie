package dev.schmarrn.schmagie.block;

import dev.schmarrn.schmagie.block.entity.ObeliskEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.swing.text.html.BlockView;

public class Obelisk extends Block implements BlockEntityType.BlockEntitySupplier {
	private Block base;

	public Obelisk(Block base) {
		super(BlockBehaviour.Properties.copy(base));

		this.base = base;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.create(0.125, 0, 0.0625, 0.875, 1, 0.9375));
		shape = Shapes.or(shape, Shapes.create(0.0625, 0, 0.125, 0.125, 1, 0.875));
		shape = Shapes.or(shape, Shapes.create(0.875, 0, 0.125, 0.9375, 1, 0.875));

		return shape;
	}

	@Override
	public BlockEntity create(BlockPos pos, BlockState state) {
		return new ObeliskEntity(pos, state);
	}
}
