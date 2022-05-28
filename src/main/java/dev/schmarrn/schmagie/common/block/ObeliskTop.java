package dev.schmarrn.schmagie.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ObeliskTop extends Block {
	public ObeliskTop(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.375, 0.3125, 0.3125, 0.5, 0.6875));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.3125, 0.375, 0.25, 0.6875, 0.5, 0.75));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.6875, 0.375, 0.3125, 0.75, 0.5, 0.6875));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.25, 0.25, 0.25, 0.375, 0.75));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.25, 0.1875, 0.75, 0.375, 0.8125));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0.25, 0.25, 0.8125, 0.375, 0.75));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.125, 0.1875, 0.1875, 0.25, 0.8125));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.125, 0.125, 0.8125, 0.25, 0.875));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.8125, 0.125, 0.1875, 0.875, 0.25, 0.8125));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0, 0.125, 0.125, 0.125, 0.875));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.0625, 0.875, 0.125, 0.9375));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0, 0.125, 0.9375, 0.125, 0.875));

		return shape;
	}
}
