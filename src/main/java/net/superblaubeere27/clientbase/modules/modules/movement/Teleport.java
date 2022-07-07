package net.superblaubeere27.clientbase.modules.modules.movement;

import org.lwjgl.input.Mouse;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

public class Teleport extends Module {

	public Teleport() {
		super("Teleport", "Click to tp", ModuleCategory.MOVEMENT);
	}
	
    private ModeValue modes = new ModeValue("Fly modes", "Vanilla", "Vanilla","AAC3", "Flag");
	
    @Override
    public void onDisable() {
    	if(mc.thePlayer == null) return;
    	
        mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
    }
    
    public MovingObjectPosition rayTrace(double blockReachDistance) {
        Vec3 vec3 = this.mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec4 = this.mc.thePlayer.getLookVec();
        Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.mc.theWorld.rayTraceBlocks(vec3, vec5, !this.mc.thePlayer.isInWater(), false, false);
    }

    public void setPos(double x, double y, double z) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.mc.thePlayer.setPositionAndUpdate(x, y, z);
    }
    
    void forward(Double length) {
        EntityPlayerSP thePlayer = mc.thePlayer;
        double yaw = Math.toRadians(thePlayer.rotationYaw);
        thePlayer.setPosition(thePlayer.posX + -Math.sin(yaw) * length, thePlayer.posY, thePlayer.posZ + Math.cos(yaw) * length);
    }
    
	@EventTarget
    public void onMotionUpdate(MotionUpdateEvent e) {
		if(modes.getObject() == 0) {
			MovingObjectPosition ray = this.rayTrace(500.0);
	        if (ray == null) {
	            return;
	        }
	        if (Mouse.isButtonDown((int)1)) {
	            double x_new = (double)ray.getBlockPos().getX() + 0.5;
	            double y_new = ray.getBlockPos().getY() + 1;
	            double z_new = (double)ray.getBlockPos().getZ() + 0.5;
	            double distance = this.mc.thePlayer.getDistance(x_new, y_new, z_new);
	            double d = 0.0;
	            while (d < distance) {
	                this.setPos(this.mc.thePlayer.posX + (x_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - this.mc.thePlayer.posX) * d / distance, this.mc.thePlayer.posY + (y_new - this.mc.thePlayer.posY) * d / distance, this.mc.thePlayer.posZ + (z_new - (double)this.mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - this.mc.thePlayer.posZ) * d / distance);
	                d += 2.0;
	            }
	            this.setPos(x_new, y_new, z_new);
	            this.mc.renderGlobal.loadRenderers();
	        }
		}
		
		if(modes.getObject() == 1) {
			if(!Mouse.isButtonDown(1)) return;
			
			 if (mc.thePlayer.onGround)
	                mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.2D, mc.thePlayer.posZ);

	            final float vanillaSpeed = 2F;

	            mc.thePlayer.capabilities.isFlying = false;

	            mc.thePlayer.motionX = 0.0;
	            mc.thePlayer.motionY = 0.0;
	            mc.thePlayer.motionZ = 0.0;

	            if (mc.gameSettings.keyBindJump.isKeyDown())
	                mc.thePlayer.motionY = mc.thePlayer.motionY + vanillaSpeed;
	            if (mc.gameSettings.keyBindSneak.isKeyDown())
	                mc.thePlayer.motionY = mc.thePlayer.motionY - vanillaSpeed;
	            Fly.strafe(vanillaSpeed);
		}
		
		if(modes.getObject() == 2) {
			if(Mouse.isButtonDown(1)) {
				// Sneak
				EntityPlayerSP thePlayer = mc.thePlayer;
	            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));

				MovingObjectPosition ray = this.rayTrace(500.0);
	            double endX = (double)ray.getBlockPos().getX() + 0.5;
	            double endY = ray.getBlockPos().getY() + 1;
	            double endZ = (double)ray.getBlockPos().getZ() + 0.5;
	            // Teleport
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(endX, endY, endZ, true));
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY + 5D, thePlayer.posZ, true));
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(endX, endY, endZ, true));
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX + 0.5D, thePlayer.posY, thePlayer.posZ + 0.5D, true));

	            forward(0.04D);

	            // Sneak
	            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
	            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
			}
		}
	}

}
