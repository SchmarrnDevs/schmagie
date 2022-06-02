package dev.schmarrn.schmagie.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ObeliskBase extends Block {
	private Block base;

	public ObeliskBase(Block base) {
		super(AbstractBlock.Settings.copy(base));

		this.base = base;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.9375, 0.0625, 0.9375, 1, 0.9375));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, 0.9375, 1));

		return shape;
	}
}
