package dev.schmarrn.schmagie.common.block;

import java.util.Random;

import dev.schmarrn.schmagie.common.Schmagie;
import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Obelisk extends Block implements BlockEntityProvider {
	public static final BooleanProperty SYNC_HACK = BooleanProperty.of("sync_hack");

	public Obelisk(Settings settings) {
		super(settings);

		setDefaultState(getStateManager().getDefaultState().with(SYNC_HACK, false));
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(SYNC_HACK);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (hit.getSide() == Direction.DOWN || hit.getSide() == Direction.UP) {
			return super.onUse(state, world, pos, player, hand, hit);
		}
		if (!world.isClient && player.getStackInHand(hand).getItem() instanceof DyeItem item && world.getBlockEntity(pos) instanceof ObeliskEntity e) {
			e.setColor(hit.getSide(), item.getColor());
			e.randomRune(hit.getSide());
			e.markDirty();
			e.sync(); // This *should* sync to the client, but it doesn't.

			if (!player.isCreative()) {
				player.getStackInHand(hand).decrement(1);
			}

			// This is an ugly workaround.
			// For the love of god, I won't able to get the client to render
			// the changed color. I just couldn't. But it did work back when
			// I experimented around with block-states. So, first I tried
			// to simply toggle a block-state Boolean right here, but that
			// didn't work. It changed the colors with one `onUse` delay.
			// So, I've put the block-state toggle inside scheduleBlockTick,
			// which gets scheduled one Tick after `onUse`.
			// According to my very limited testing, this should work now.
			// But if there is a better way to do this, let's do that instead.
			// This is an ugly hack and I hate it.
			world.scheduleBlockTick(pos, state.getBlock(), 1);
		}

		return ActionResult.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(SYNC_HACK, !state.get(SYNC_HACK)));
		super.scheduledTick(state, world, pos, random);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ObeliskEntity(pos, state);
	}
}
