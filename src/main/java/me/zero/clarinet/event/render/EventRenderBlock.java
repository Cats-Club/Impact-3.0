package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public class EventRenderBlock implements Event {
	
	public final IBlockState state;
	public final Block block;
	public final BlockPos pos;
	
	public EventRenderBlock(IBlockState state, BlockPos pos) {
		this.state = state;
		this.block = state.getBlock();
		this.pos = pos;
	}
}
