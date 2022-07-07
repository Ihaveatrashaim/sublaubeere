package net.superblaubeere27.clientbase.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BBEvent implements Event {
    public double x;
    public double y;
    public double z;
    public BlockPos blockPos;
    public Block block;
    public AxisAlignedBB boundingBox;
    
    public BBEvent(BlockPos blockPos, Block block, AxisAlignedBB boundingBox, double x, double y, double z) {
		this.blockPos = blockPos;
		this.block = block;
		this.boundingBox = boundingBox;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}