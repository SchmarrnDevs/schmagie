package dev.schmarrn.schmagie.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ObeliskTop extends Block {
	private Block base;

	public ObeliskTop(Block base) {
		super(BlockBehaviour.Properties.copy(base));

		this.base = base;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.create(0.25, 0.375, 0.3125, 0.3125, 0.5, 0.6875));
		shape = Shapes.or(shape, Shapes.create(0.3125, 0.375, 0.25, 0.6875, 0.5, 0.75));
		shape = Shapes.or(shape, Shapes.create(0.6875, 0.375, 0.3125, 0.75, 0.5, 0.6875));
		shape = Shapes.or(shape, Shapes.create(0.1875, 0.25, 0.25, 0.25, 0.375, 0.75));
		shape = Shapes.or(shape, Shapes.create(0.25, 0.25, 0.1875, 0.75, 0.375, 0.8125));
		shape = Shapes.or(shape, Shapes.create(0.75, 0.25, 0.25, 0.8125, 0.375, 0.75));
		shape = Shapes.or(shape, Shapes.create(0.125, 0.125, 0.1875, 0.1875, 0.25, 0.8125));
		shape = Shapes.or(shape, Shapes.create(0.1875, 0.125, 0.125, 0.8125, 0.25, 0.875));
		shape = Shapes.or(shape, Shapes.create(0.8125, 0.125, 0.1875, 0.875, 0.25, 0.8125));
		shape = Shapes.or(shape, Shapes.create(0.0625, 0, 0.125, 0.125, 0.125, 0.875));
		shape = Shapes.or(shape, Shapes.create(0.125, 0, 0.0625, 0.875, 0.125, 0.9375));
		shape = Shapes.or(shape, Shapes.create(0.875, 0, 0.125, 0.9375, 0.125, 0.875));

		return shape;
	}
}
