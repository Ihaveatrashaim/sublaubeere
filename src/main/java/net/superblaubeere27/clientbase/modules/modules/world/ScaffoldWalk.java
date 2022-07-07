
package net.superblaubeere27.clientbase.modules.modules.world;

import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
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
import net.superblaubeere27.clientbase.valuesystem.Value;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

/*
 * Copyright (c) 2018 superblaubeere27

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class ScaffoldWalk extends Module {
    public ScaffoldWalk() {
		super("Scaffold", "Automatic make a bridge", ModuleCategory.WORLD, true, false, Keyboard.KEY_X);
	}


	private BlockPos currentPos;
    private EnumFacing currentFacing;
    private boolean rotated = false;
    private TimeHelper timer = new TimeHelper();
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 0, 0, 2000);
    private NumberValue<Integer> expand = new NumberValue<>("Expand value", 1, 1, 6);
    private BooleanValue eagle = new BooleanValue("Eagle", false);
    private BooleanValue raycast = new BooleanValue("RayCast", true);
    private BooleanValue keepRotations = new BooleanValue("KeepRotations", true);
    private ModeValue modes = new ModeValue("Scaffold Mode", "SimpleNCP", "SimpleNCP");
    private float[] rotations = new float[2];
    
    
    private void findBlock(Boolean expand, int expandValue) {
        EntityPlayerSP player = mc.thePlayer;

        if (expand) {
            double yaw = Math.toRadians(player.rotationYaw);
            double x = -Math.sin(yaw);
            double z = Math.cos(yaw);
            for(int i = 0; i < expandValue; i++) {
            	currentPos.add(x * i, 0, z * i);
            }
        }
    }
    
    @EventTarget
    public void onMotionUpdate(MotionUpdateEvent e) {
        if (!getState()) return;


        if (eagle.getObject()) {
            if (rotated) {
                setSneaking(true);
            } else {
                setSneaking(false);
            }
        }
        
        if(modes.getObject() == 0) {
        	if (e.getEventType() == EventType.PRE) {
                rotated = false;
                currentPos = null;
                currentFacing = null;
                
                BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
                if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
                    setBlockAndFacing(pos);
                    if (currentPos != null) {
                    	mc.thePlayer.onGround = true;
                    	mc.thePlayer.fallDistance = 0.0f;
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
            	TimeHelper t = new TimeHelper();
                if (currentPos != null) {
                    if (timer.hasTimeReached(delay.getObject()) && rotated) {
                    	this.setSneaking(false);
            			currentPos.offset(mc.thePlayer.getHorizontalFacing(), 1);
                    	if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                    		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));
                    		if(t.hasReached(4)) {
                        		mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));
                        		t.reset();
                    		}
                    		timer.setLastMS();
                            mc.thePlayer.swingItem();
                            currentPos.add(1, 0, 1);
                            if (!eagle.getObject()) {
                                mc.thePlayer.motionX = 0;
                                mc.thePlayer.motionZ = 0;
                            } else {
                                mc.thePlayer.motionX *= 0.7;
                                mc.thePlayer.motionZ *= 0.7;
                            }
                        }
                    }else {
                    	this.setSneaking(true);
                    }
                }
            }
        }
    }
    
    private boolean isValid(Item item) {
        return item instanceof ItemBlock;
    }
    
    private int getBlockSlot() {
        int slot = -1;
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            for (int i = 36; i < 45; ++i) {
                slot = i - 36;
                if (!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), new ItemStack(Item.getItemById(261)), true) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && this.isValid(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0) {
                    break;
                }
            }
        } else {
            slot = mc.thePlayer.inventory.currentItem;
        }
        return slot;
    }
    
    private void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
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
