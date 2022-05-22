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

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, 0.125, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.25, 0.125, 0.875, 0.375, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.375, 0.1875, 0.8125, 0.5, 0.8125));

        return shape;
    }
}
