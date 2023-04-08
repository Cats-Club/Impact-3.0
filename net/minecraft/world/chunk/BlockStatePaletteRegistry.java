package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;

public class BlockStatePaletteRegistry implements IBlockStatePalette
{
    public int idFor(IBlockState state)
    {
        int i = Block.BLOCK_STATE_IDS.get(state);
        return i == -1 ? 0 : i;
    }

    /**
     * Gets the block state by the palette id.
     */
    public IBlockState getBlockState(int indexKey)
    {
        IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(indexKey);
        return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
    }

    public void read(PacketBuffer buf)
    {
        buf.readVarIntFromBuffer();
    }

    public void write(PacketBuffer buf)
    {
        buf.writeVarIntToBuffer(0);
    }

    public int getSerializedState()
    {
        return PacketBuffer.getVarIntSize(0);
    }
}
