package dev.schmarrn.schmagie.block.entity;


import dev.schmarrn.schmagie.Schmagie;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ObeliskEntity extends BlockEntity implements RenderAttachmentBlockEntity {
	public static class Data {
		private int[] color = new int[4];
		private int[] rune = {-1, -1, -1, -1};

		public int[] getColor() {
			return color;
		}

		public DyeColor getColor(Direction dir) {
			return DyeColor.byId(this.color[dir.get2DDataValue()]);
		}

		public int[] getRune() {
			return rune;
		}

		public int getRune(Direction dir) {
			if (this.rune.length != 4) return 0;
			return this.rune[dir.get2DDataValue()];
		}
	}

	private final Data data = new Data();

	public ObeliskEntity(BlockPos blockPos, BlockState blockState) {
		super(SchmagieBlockEntities.OBELISK_BLOCK_ENTITY, blockPos, blockState);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putIntArray("color", data.color);
		nbt.putIntArray("rune", data.rune);
		super.saveAdditional(nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		data.color = nbt.getIntArray("color");
		data.rune = nbt.getIntArray("rune");

		// If we are on the client, re-render the Block because of the changed NBT Data
		if (level != null && level.isClientSide()) {
			Minecraft client = Minecraft.getInstance();
			client.execute(() -> {
				Schmagie.LOGGER.info("Update Client Block");
				client.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
			});
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.hasLevel()) {
			if (!level.isClientSide()) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
			}
		}
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		saveAdditional(nbt);
		return nbt;
	}

	public void setColor(Direction dir, DyeColor color) {
		data.color[dir.get2DDataValue()] = color.getId();
	}

	public void assignRandomRune(Direction dir) {
		setRune(dir, new Random().nextInt(0, 8));
	}

	public void setRune(Direction dir, int i) {
		if (data.rune.length != 4 || i < 0 || i >= 8) return;
		data.rune[dir.get2DDataValue()] = i;
	}

	public void onDyeUse(Direction dir, DyeColor color) {
		this.setColor(dir, color);
		this.assignRandomRune(dir);
		this.setChanged();
	}

	@Override
	public Data getRenderAttachmentData() {
		return data;
	}
}
