package dev.schmarrn.schmagie.common.block;

import java.util.Random;

import dev.schmarrn.schmagie.common.block.entity.ObeliskEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Obelisk extends Block implements BlockEntityProvider {
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    public Obelisk(Settings settings) {
        super(settings);

        setDefaultState(getStateManager().getDefaultState().with(ACTIVATED, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {

        if (!world.isClient && player.getStackInHand(hand).getItem() instanceof DyeItem) {
            ObeliskEntity e = (ObeliskEntity) world.getBlockEntity(pos);
            DyeItem stack = (DyeItem)player.getStackInHand(hand).getItem();
            
            e.setColor(stack.getColor());
            e.markDirty();
            e.sync(); // This *should* sync, but it doesn't.

            // This is an ugly workaround.
            // For the love of god, I wan't able to get the client to render
            // the changed color. I just couldn't. But it did work back when
            // I experimented around with blockstates. So, first I tried
            // to simply toggle a Blockstate Boolean right here, but that
            // didn't work. It changed the colors with one `onUse` delay.
            // So, I've put the Blockstate toggle inside of scheduleBlockTick,
            // which gets scheduled one Tick after `onUse`.
            // According to my very limited testing, this should work now.
            // But if there is a better way to do this, let's do that instead.
            // This is an ugly hack and I hate it.
            world.scheduleBlockTick(pos, state.getBlock(), 1);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(ACTIVATED, !state.get(ACTIVATED)));
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ObeliskEntity(pos, state);
    }
}
