/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.modules.modules.movement.Fly;
import net.superblaubeere27.clientbase.notifications.Notification;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.notifications.NotificationType;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

import java.util.ArrayList;
import java.util.List;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Remove Fall Damage", ModuleCategory.WORLD);
    }

    private ModeValue mode = new ModeValue("NoFall Modes", "Packet", "Packet", "AAC", "Matrix");

	private boolean PacketSend;
    
    public String getMode(){
        int Mode = mode.getObject();
        if(Mode == 0) return "Packet";
        if (Mode == 1) return "AAC";
        if (Mode == 2) return "Matrix";

        else return null;
    }
    
    @EventTarget
    private void onMove(MotionUpdateEvent event) {
    	if(!getState()) return;
    	
    	this.setName("NoFall" + "§7 " + getMode());
    	
    	if(getMode().equalsIgnoreCase("Matrix")) {
    		Fly.setTimerSpeed(1F);

    		boolean matrixIsFall = true;

    		if (mc.thePlayer.fallDistance-mc.thePlayer.motionY>3) {
    			mc.thePlayer.onGround = true;
    			mc.thePlayer.fallDistance = 0.0F;
    			PacketSend = true;
    			Fly.setTimerSpeed(0.5f);
    		}
    	}
    	
    	if(getMode().equalsIgnoreCase("AAC")) {
    		if(!mc.thePlayer.onGround) {
        		mc.thePlayer.motionY = -5;
    		}
    	}
    	
    	if(getMode().equalsIgnoreCase("Packet")) {
    		if(mc.thePlayer.fallDistance > 3) {
    			mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        		mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
        		mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
        		mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
        		mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
    		}
    	}
    	
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if(getMode().equalsIgnoreCase("Matrix")) {
        	Packet packet = event.getPacket();
        	if (event.getPacket() instanceof C03PacketPlayer) {
    			if (PacketSend) {
    				PacketSend = false;
    				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) packet).getPositionX(), ((C03PacketPlayer) packet).getPositionY(), ((C03PacketPlayer) packet).getPositionZ(), true));
    				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((C03PacketPlayer) packet).getPositionX(), ((C03PacketPlayer) packet).getPositionY(), ((C03PacketPlayer) packet).getPositionZ(), false));
    				event.setCancelled(true);
    			}
    		}
        }
    }
}
