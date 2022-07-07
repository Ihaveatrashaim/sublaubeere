package net.superblaubeere27.clientbase.injection.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.superblaubeere27.clientbase.events.BBEvent;


@Mixin(Block.class)
public abstract class MixinBlock {
	@Shadow
    @Final
    protected BlockState blockState;

    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state);

    @Shadow
    public abstract void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ);

    // Has to be implemented since a non-virtual call on an abstract method is illegal
    @Shadow
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return null;
    }
	
    @Overwrite
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    	AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
        BBEvent blockBBEvent = new BBEvent(pos, blockState.getBlock(), axisalignedbb, pos.getX(), pos.getY(), pos.getZ());
        EventManager.call(blockBBEvent);

        axisalignedbb = blockBBEvent.boundingBox;
        
        if (axisalignedbb != null && mask.intersectsWith(axisalignedbb))list.add(axisalignedbb);
    }
}
