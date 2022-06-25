
package net.superblaubeere27.clientbase.modules.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.BlockUtil;
import net.superblaubeere27.clientbase.utils.RayCastUtil;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import org.lwjgl.input.Keyboard;


import java.lang.reflect.Field;

public class ScaffoldWalk extends Module {
    private BlockPos currentPos;
    private EnumFacing currentFacing;
    private boolean rotated = false;
    private TimeHelper timer = new TimeHelper();
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 450, 0, 2000);
    private BooleanValue eagle = new BooleanValue("Eagle", false);
    private BooleanValue raycast = new BooleanValue("RayCast", true);
    private BooleanValue keepRotations = new BooleanValue("KeepRotations", true);

    private float[] rotations = new float[2];

    public ScaffoldWalk() {
        super("ScaffoldWalk", "WEEEE making a bridge", ModuleCategory.WORLD, true, false, Keyboard.KEY_X);
    }


    @EventTarget
    public void onMotionUpdate(MotionUpdateEvent e) {
        if (!getState()) return;

        NCP(e);

    }

    private void Expand(MotionUpdateEvent e){
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        BlockPos pos1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ).offset(mc.thePlayer.getHorizontalFacing(), 1);
        BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ).offset(mc.thePlayer.getHorizontalFacing(), 2);
        BlockPos pos3 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ).offset(mc.thePlayer.getHorizontalFacing(), 3);

        if(!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) return;
        if(!(mc.theWorld.getBlockState(pos1).getBlock() instanceof BlockAir)) return;
        if(!(mc.theWorld.getBlockState(pos2).getBlock() instanceof BlockAir)) return;
        if(!(mc.theWorld.getBlockState(pos3).getBlock() instanceof BlockAir)) return;

        if(e.getEventType() != EventType.POST) return;

        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
            setSneaking(false);
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos3, currentFacing, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos1, currentFacing, new Vec3(pos1.getX(), pos1.getY(), pos1.getZ()));

            mc.thePlayer.swingItem();

            if (!eagle.getObject()) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            } else {
                mc.thePlayer.motionX *= 0.7;
                mc.thePlayer.motionZ *= 0.7;
            }
        }
    }

    private void NCP(MotionUpdateEvent e){
        if (eagle.getObject()) {
            if (rotated) {
                setSneaking(true);
            } else {
                setSneaking(false);
            }
        }

        if (e.getEventType() == EventType.PRE) {
            rotated = false;
            currentPos = null;
            currentFacing = null;

            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
                setBlockAndFacing(pos);

                if (currentPos != null) {
                    float facing[] = BlockUtil.getDirectionToBlock(currentPos.getX(), currentPos.getY(), currentPos.getZ(), currentFacing);

                    float yaw = facing[0];
                    float pitch = Math.min(90, facing[1] + 9);

                    rotations[0] = yaw;
                    rotations[1] = pitch;

                    rotated = !raycast.getObject() || rayTrace(yaw, pitch);

                    e.setYaw(yaw);
                    e.setPitch(pitch);
                }
            } else {
                if (keepRotations.getObject()) {
                    e.setYaw(rotations[0]);
                    e.setPitch(rotations[1]);
                }
            }
            mc.thePlayer.rotationYawHead = e.getYaw();
        }
        if (e.getEventType() == EventType.POST) {
            if (currentPos != null) {
                if (timer.hasTimeReached(delay.getObject()) && rotated) {
                    if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                        setSneaking(false);
                        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))) {
                            timer.setLastMS();
                            mc.thePlayer.swingItem();

                            if (!eagle.getObject()) {
                                mc.thePlayer.motionX = 0;
                                mc.thePlayer.motionZ = 0;
                            } else {
                                mc.thePlayer.motionX *= 0.7;
                                mc.thePlayer.motionZ *= 0.7;
                            }
                        }
                    }
                }else{
                    setSneaking(true);
                }
            }
        }
    }

    private boolean rayTrace(float yaw, float pitch) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = RayCastUtil.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * 5, vec31.yCoord * 5, vec31.zCoord * 5);


        MovingObjectPosition result = mc.theWorld.rayTraceBlocks(vec3, vec32, false);


        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && currentPos.equals(result.getBlockPos());
    }

    private void setSneaking(boolean b) {
        KeyBinding sneakBinding = mc.gameSettings.keyBindSneak;
        try {
            Field field = sneakBinding.getClass().getDeclaredField("pressed");
            field.setAccessible(true);
            field.setBoolean(sneakBinding, b);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        setSneaking(false);
    }


    private void setBlockAndFacing(BlockPos var1) {
        if (mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, -1, 0);
            currentFacing = EnumFacing.UP;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(-1, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(1, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, 1);
            currentFacing = EnumFacing.NORTH;
        } else {
            currentPos = null;
            currentFacing = null;
        }
    }



}
