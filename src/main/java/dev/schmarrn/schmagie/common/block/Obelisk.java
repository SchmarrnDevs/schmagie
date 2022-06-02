package dev.schmarrn.schmagie.common.block;

import dev.schmarrn.schmagie.common.Schmagie;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Obelisk extends Block implements BlockEntityProvider {
	private Block base;

	public Obelisk(Block base) {
		super(AbstractBlock.Settings.copy(base));

		this.base = base;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = VoxelShapes.empty();
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.0625, 0.875, 1, 0.9375));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0, 0.125, 0.125, 1, 0.875));
		shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0, 0.125, 0.9375, 1, 0.875));

		return shape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		// Only handle logic if:
		//  - We have some kind of Dye in our Hands
		//  - We have an Obelisk Entity
		//  - We hit one of the sides and neither the top nor the bottom face
		if (player.getStackInHand(hand).getItem() instanceof DyeItem item &&
				world.getBlockEntity(pos) instanceof ObeliskEntity e &&
				hit.getSide() != Direction.DOWN && hit.getSide() != Direction.UP) {
			// Handle the logic on the server
			if (!world.isClient()) {
				Schmagie.LOGGER.info("Handle Obelisk Logic");
				e.setColor(hit.getSide(), item.getColor());
				e.assignRandomRune(hit.getSide());
				e.markDirty();
			}
			return ActionResult.SUCCESS;

		} else {
			// If the above conditions didn't apply, execute "normal" minecraft actions like
			// placing a block etc.
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ObeliskEntity(pos, state);
	}
}
