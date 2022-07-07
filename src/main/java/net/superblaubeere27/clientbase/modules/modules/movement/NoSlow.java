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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.BBEvent;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.events.SlowDownEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.notifications.Notification;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.notifications.NotificationType;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class NoSlow extends Module {
	
    private NumberValue<Float> speed = new NumberValue<Float>("ForwardMultiplier", 0.8F, 0.2F, 1.0F);
	
    public NoSlow() {
        super("NoSlow", "No more slowdown", ModuleCategory.COMBAT);
    }
    
    @EventTarget
    private void onMove(MotionUpdateEvent event) {
    	if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) || !Fly.isMoving())
            return;

        if (!mc.thePlayer.isBlocking())
            return;
    	
    	if(event.getEventType() == EventType.PRE) {
    		C07PacketPlayerDigging digging = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN);
    		mc.getNetHandler().addToSendQueue(digging);
    	}
    	
    	if(event.getEventType() == EventType.POST) {
    		C08PacketPlayerBlockPlacement blockPlace = new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F);
            mc.getNetHandler().addToSendQueue(blockPlace);
    	}
    }
    
    @EventTarget
    private void onSlow(SlowDownEvent e) {
    	e.forward = speed.getObject();
    	e.strafe = speed.getObject();
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
    	if(!getState()) return;
    }
    
    @EventTarget
	private void onBB(BBEvent event) {
    	
	}
}
