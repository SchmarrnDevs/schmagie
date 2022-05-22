package dev.schmarrn.schmagie.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Staff extends MiningToolItem {
    public static final float MINING_SPEED_MULTIPLIER = 2f;
    public static final float ATTACK_DAMAGE_MULTIPLIER = 1.5f;
    
    public Staff(float attackDamage, float attackSpeed, ToolMaterial toolMaterial, TagKey<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, toolMaterial, effectiveBlocks, settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof ServerPlayerEntity player) {
            if (player.isSneaking() && player.totalExperience >= 1 && stack.getDamage() != 0) {
                stack.setDamage(stack.getDamage() - 1);
                player.addExperience(-1);
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 20);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this, 20);
        }
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && user.getStackInHand(hand).isDamaged()) {
            // Required for "long usage time thing(tm)"
            return ItemUsage.consumeHeldItem(world, user, hand);
        } else {
            return super.use(world, user, hand);
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return miner.getActiveItem().getDamage() < this.getMaxDamage();
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return stack.getDamage();
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
	public boolean isSuitableFor(BlockState state) {
        int i = this.getMaterial().getMiningLevel();
		if (i < 3 && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)) {
			return false;
		} else if (i < 2 && state.isIn(BlockTags.NEEDS_IRON_TOOL)) {
			return false;
		} else {
			return i < 1 && state.isIn(BlockTags.NEEDS_STONE_TOOL) ? false : true;
		}
	}

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (stack.getDamage() >= this.getMaxDamage()) {
            return 0;
        }
        float base_speed = this.miningSpeed;
        return this.isSuitableFor(state)? base_speed * MINING_SPEED_MULTIPLIER : base_speed;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        handleDamage(stack, attacker);
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        handleDamage(stack, miner);
        return true;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage() * ATTACK_DAMAGE_MULTIPLIER;
    }


    private void handleDamage(ItemStack stack, LivingEntity user) {
        stack.setDamage(stack.getDamage() + 1);
        user.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
    }
}
