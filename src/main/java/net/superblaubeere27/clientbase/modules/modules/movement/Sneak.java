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
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
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

public class Sneak extends Module {
	private ModeValue mode = new ModeValue("Speed Modes", "Vanilla", "Vanilla", "Switch", "Legit");
    
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
	
    public String getMode(){
        int Mode = mode.getObject();
        if(Mode == 0) return "Vanilla";
        if (Mode == 1) return "Switch";
        if (Mode == 2) return "Legit";

        else return null;
    }
	
    public Sneak() {
        super("Sneak", "Sneak", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
    	if(mc.thePlayer == null) return;
    	
    	Fly.setTimerSpeed(1F);
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        setSneaking(false);
    }
    
    public void vanilla() {
        mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    }
    
    public void legit() {
    	setSneaking(true);
    }
    
    public void switchSneak(MotionUpdateEvent e) {
    	if(e.getEventType() == EventType.PRE) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    	}
    	
    	if(e.getEventType() == EventType.POST) {
    		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    	}
    }
    
    @EventTarget
    private void onMove(MotionUpdateEvent event) {
    	if(!getState()) return;
    	
    	
    	this.setName("Sneak" + "§7 " + getMode());
    	
    	if(getMode().equalsIgnoreCase("Vanilla")) {
        	setSneaking(false);
    		vanilla();
    	}
    	
    	if(getMode().equalsIgnoreCase("Legit")) {
    		legit();
    	}
        
    	if(getMode().equalsIgnoreCase("Switch")) {
        	setSneaking(false);
    		switchSneak(event);
    	}
    	
    	
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
    }
}
