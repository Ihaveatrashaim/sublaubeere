/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.notifications.Notification;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.notifications.NotificationType;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Speed extends Module {
	private ModeValue mode = new ModeValue("Speed Modes", "Bhop", "BHop", "AAC", "NCP");
    
    public String getMode(){
        int Mode = mode.getObject();
        if(Mode == 0) return "BHop";
        if (Mode == 1) return "AAC";
        if (Mode == 2) return "NCP";

        else return null;
    }
	
    public Speed() {
        super("Speed", "Make you moving faster", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
    	Fly.setTimerSpeed(1F);
    }
    
    private void setJump(boolean b) {
        KeyBinding jumpBinding = mc.gameSettings.keyBindJump;
        try {
            Field field = jumpBinding.getClass().getDeclaredField("pressed");
            field.setAccessible(true);
            field.setBoolean(jumpBinding, b);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @EventTarget
    private void onMove(MotionUpdateEvent event) {
    	if(!getState())  return;
    	
    	this.setName("Speed" + "§7 " + getMode());
    	
    	if(getMode().equalsIgnoreCase("Bhop")) {
    		if(!Fly.isMoving()) {
                Fly.setTimerSpeed(1F);
    			
    			return;
    		}
    		
        	Fly.setTimerSpeed(1.08F);

    		
    		if(mc.thePlayer.onGround) {
            	setJump(true);
            	float f = mc.thePlayer.rotationYaw * 0.017453292F;
                Fly.setTimerSpeed(1.18F);
            }else {
            	setJump(false);

            }
    	}
    	
    	
    	
    	if(getMode().equalsIgnoreCase("AAC")) {
    		if(Fly.isMoving()) {
    			Fly.setTimerSpeed(1.5F);
    			mc.getNetHandler().addToSendQueue(new
    		            C03PacketPlayer.C04PacketPlayerPosition(
    		                mc.thePlayer.posX,
    		                mc.thePlayer.posY,
    		                mc.thePlayer.posZ,
    		                true
    		            )
    		        );
    		}
    	}
    	
    	if(getMode().equalsIgnoreCase("NCP")) {
    		EntityPlayer thePlayer = mc.thePlayer;

    		if (mc.thePlayer.isOnLadder() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || !(Fly.isMoving()) || mc.thePlayer.isInWater()) return;
    		        if (mc.thePlayer.onGround) {;
    		        	setJump(true);
    		}
    		Fly.strafe(0.4F);
    	}
        
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
    }
}
