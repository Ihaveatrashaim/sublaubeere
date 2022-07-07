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

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
	private ModeValue mode = new ModeValue("Speed Modes", "Bhop", "BHop", "AAC", "NCP", "Hypixel", "AAC4");
    
    public String getMode(){
        int Mode = mode.getObject();
        if(Mode == 0) return "BHop";
        if (Mode == 1) return "AAC";
        if (Mode == 2) return "NCP";
        if(Mode == 3) return "Hypixel";
        if(Mode == 4) return "AAC4";


        else return null;
    }
	
    public Speed() {
        super("Speed", "Make you moving faster", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
    	if(mc.thePlayer == null) return;
    	
    	Fly.setTimerSpeed(1F);
    	mc.thePlayer.setSilent(false);
    	this.setJump(false);
    }
    
    public static void setJump(boolean b) {
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
    	    
    	this.setDisplayName("Speed" + "§7 " + getMode());
    	
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
    			//Fake regen to bypass timer check
    			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 2, 5));
    			Fly.setTimerSpeed(1.18F);
    		}else {
    			Fly.setTimerSpeed(1F);
    		}
    	}
    	
    	if(getMode().equalsIgnoreCase("NCP")) {
    		EntityPlayer thePlayer = mc.thePlayer;
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 2, 5));
    		if(mc.gameSettings.keyBindForward.isKeyDown()) {
    			Fly.setTimerSpeed(1.21F);    			
    		}else {
    			mc.thePlayer.motionY = -0.5;
    		}
    	}
    	
    	if(getMode().equalsIgnoreCase("Hypixel")) {
    		if (mc.theWorld != null && mc.thePlayer != null) {
    			this.setJump(false);
    			if (mc.gameSettings.keyBindForward.isKeyDown() && !mc.thePlayer.isCollidedHorizontally) {
    				if (mc.thePlayer.onGround) {
    					mc.thePlayer.jump();
    					Fly.setTimerSpeed(1.05F);
    				} else {
    					mc.thePlayer.jumpMovementFactor = 0.0265F;
    					Fly.setTimerSpeed(1.4F);
    				}
    			}
    		}
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	if(getMode().equalsIgnoreCase("AAC4")) {
    		//Fake
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 2, 5));
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 2, 5));

    		if(this.isMoving()) {
    	    	mc.thePlayer.setSilent(false);
    			if(mc.thePlayer.onGround) {
        			this.setTimerSpeed(2.12F);
    				setJump(true);
    			}else {
    				

    			}
    			this.setTimerSpeed(1F);

    		}else {
				setJump(false);
    		}
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
       
    }
    
    public static void setTimerSpeed(float timerSpeed){
        try{
            Class<?> mc = Minecraft.class;
            Field timerField = mc.getDeclaredField("timer");
            timerField.setAccessible(true);

            try{
                Object timer = timerField.get(Minecraft.getMinecraft());
                Class<?> timerClass = timer.getClass();
                Field timerSpeedField = timerClass.getDeclaredField("timerSpeed");
                timerSpeedField.setAccessible(true);
                timerSpeedField.setFloat(timer, timerSpeed);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }

        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isMoving() {
        if (mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f)) {
            return true;
        }else {
            return false;
        }
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
    }
}
