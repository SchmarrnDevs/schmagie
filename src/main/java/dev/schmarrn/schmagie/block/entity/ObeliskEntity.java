package dev.schmarrn.schmagie.block.entity;

import javax.annotation.Nullable;

import dev.schmarrn.schmagie.Schmagie;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class ObeliskEntity extends BlockEntity implements RenderAttachmentBlockEntity {
	public static class Data {
		private int[] color = new int[4];
		private int[] rune = {-1, -1, -1, -1};

		public int[] getColor() {
			return color;
		}

		public DyeColor getColor(Direction dir) {
			return DyeColor.byId(this.color[dir.getId() - 2]);
		}

		public int[] getRune() {
			return rune;
		}

		public int getRune(Direction dir) {
			if (this.rune.length != 4) return 0;
			return this.rune[dir.getId() - 2];
		}
	}

	private final Data data = new Data();

	public ObeliskEntity(BlockPos blockPos, BlockState blockState) {
		super(SchmagieBlockEntities.OBELISK_BLOCK_ENTITY, blockPos, blockState);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		nbt.putIntArray("color", data.color);
		nbt.putIntArray("rune", data.rune);
		super.writeNbt(nbt);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		data.color = nbt.getIntArray("color");
		data.rune = nbt.getIntArray("rune");

		// If we are on the client, re-render the Block because of the changed NBT Data
		if (world != null && world.isClient()) {
			MinecraftClient client = MinecraftClient.getInstance();
			client.execute(() -> {
				Schmagie.LOGGER.info("Update Client Block");
				client.world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
			});
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (this.hasWorld()) {
			if (!world.isClient()) {
				Schmagie.LOGGER.info("markDirty");
				((ServerWorld) world).getChunkManager().markForUpdate(pos);
				world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
			}
		}
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		writeNbt(nbt);
		return nbt;
	}


	public void setColor(Direction dir, DyeColor color) {
		data.color[dir.getId() - 2] = color.getId();
	}

	public void assignRandomRune(Direction dir) {
		setRune(dir, new Random().nextInt(0, 8));
	}

	public void setRune(Direction dir, int i) {
		if (data.rune.length != 4 || i < 0 || i >= 8) return;
		data.rune[dir.getId() - 2] = i;
	}

	@Override
	public Data getRenderAttachmentData() {
		return data;
	}
}
