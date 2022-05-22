package dev.schmarrn.schmagie.common.block.entity;

import javax.annotation.Nullable;

import dev.schmarrn.schmagie.common.Schmagie;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ObeliskEntity extends BlockEntity implements RenderAttachmentBlockEntity {
    private int[] color = new int[4];

    public ObeliskEntity(BlockPos blockPos, BlockState blockState) {
        super(Schmagie.OBELISK_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putIntArray("color", color);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        color = nbt.getIntArray("color");
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

    public DyeColor getColor(Direction dir) {
        return DyeColor.byId(color[dir.getId()-2]);
    }

    public void setColor(Direction dir, DyeColor color) {
        this.color[dir.getId()-2] = color.getId();
    }

    public void sync() {
        if (this.hasWorld() && !this.getWorld().isClient) {
            ((ServerWorld) world).getChunkManager().markForUpdate(getPos());
            ((ServerWorld) world).updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public int[] getRenderAttachmentData() {
        return color;
    }
}
