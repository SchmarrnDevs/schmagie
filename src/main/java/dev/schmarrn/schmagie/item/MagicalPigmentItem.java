package dev.schmarrn.schmagie.item;

import dev.schmarrn.schmagie.block.entity.ObeliskEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MagicalPigmentItem extends Item {
	private final DyeColor color;
	public MagicalPigmentItem(Settings settings, DyeColor color) {
		super(settings);

		this.color = color;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		// Only handle logic if:
		//  - We use on an obelisk block entity
		//  - We hit one of the sides, not the top or bottom
		//  - We are on the server
		// If those conditions don't apply, pass.
		BlockPos pos = context.getBlockPos();
		Direction dir = context.getSide();

		if (context.getWorld().getBlockEntity(pos) instanceof ObeliskEntity obelisk &&
			dir != Direction.DOWN && dir != Direction.UP) {
			if (!context.getWorld().isClient) {
				obelisk.onDyeUse(dir, color);
			}
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.PASS;
		}
	}

	public DyeColor getColor() {
		return color;
	}
}
