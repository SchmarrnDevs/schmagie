package dev.schmarrn.schmagie.item;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.state.BlockState;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class Staff extends DiggerItem {
    public static final float MINING_SPEED_MULTIPLIER = 2f;
    public static final float ATTACK_DAMAGE_MULTIPLIER = 1.5f;

    public Staff(float attackDamage, float attackSpeed, Tier toolMaterial, TagKey<Block> effectiveBlocks, Properties settings) {
        super(attackDamage, attackSpeed, toolMaterial, effectiveBlocks, settings);
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof ServerPlayer player) {
            if (player.isCrouching() && player.totalExperience >= 1 && stack.getDamageValue() != 0) {
                stack.setDamageValue(stack.getDamageValue() - 1);
                player.giveExperiencePoints(-1);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player) {
            player.getCooldowns().addCooldown(this, 20);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (user instanceof Player player) {
            player.getCooldowns().addCooldown(this, 20);
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (user.isCrouching() && user.getItemInHand(hand).isDamaged()) {
            // Required for "long usage time thing(tm)"
            return ItemUtils.startUsingInstantly(world, user, hand);
        } else {
            return super.use(world, user, hand);
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
        return miner.getMainHandItem().getDamageValue() < this.getMaxDamage();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getDamageValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
	public boolean isCorrectToolForDrops(BlockState state) {
        int i = this.getTier().getLevel();
		if (i < 3 && state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
			return false;
		} else if (i < 2 && state.is(BlockTags.NEEDS_IRON_TOOL)) {
			return false;
		} else {
			return i < 1 && state.is(BlockTags.NEEDS_STONE_TOOL) ? false : true;
		}
	}

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getDamageValue() >= this.getMaxDamage()) {
            return 0;
        }
        float base_speed = this.speed;
        return this.isCorrectToolForDrops(state)? base_speed * MINING_SPEED_MULTIPLIER : base_speed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        handleDamage(stack, attacker);
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
        handleDamage(stack, miner);
        return true;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage() * ATTACK_DAMAGE_MULTIPLIER;
    }


    private void handleDamage(ItemStack stack, LivingEntity user) {
        stack.setDamageValue(stack.getDamageValue() + 1);
        user.playSound(SoundEvents.ITEM_BREAK, 1.0f, 1.0f);
    }
}
