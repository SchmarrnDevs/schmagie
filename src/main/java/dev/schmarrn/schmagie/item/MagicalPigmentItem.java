package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.block.entity.ObeliskEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class MagicalPigmentItem extends Item {
	private final DyeColor color;
	public MagicalPigmentItem(Properties settings, DyeColor color) {
		super(settings);

		this.color = color;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		// Only handle logic if:
		//  - We use on an obelisk block entity
		//  - We hit one of the sides, not the top or bottom
		//  - We are on the server
		// If those conditions don't apply, pass.
		BlockPos pos = context.getClickedPos();
		Direction dir = context.getHorizontalDirection();

		if (context.getLevel().getBlockEntity(pos) instanceof ObeliskEntity obelisk &&
			dir != Direction.DOWN && dir != Direction.UP) {
			if (!context.getLevel().isClientSide) {
				obelisk.onDyeUse(dir, color);
			}
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	public DyeColor getColor() {
		return color;
	}
}
