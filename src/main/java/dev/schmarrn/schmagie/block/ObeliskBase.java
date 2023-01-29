package dev.schmarrn.schmagie.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ObeliskBase extends Block {
	private Block base;

	public ObeliskBase(Block base) {
		super(BlockBehaviour.Properties.copy(base));

		this.base = base;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.or(shape, Shapes.create(0.0625, 0.9375, 0.0625, 0.9375, 1, 0.9375));
		shape = Shapes.or(shape, Shapes.create(0, 0, 0, 1, 0.9375, 1));

		return shape;
	}
}
