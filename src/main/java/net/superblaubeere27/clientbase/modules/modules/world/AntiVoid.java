package net.superblaubeere27.clientbase.modules.modules.world;


import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.modules.modules.movement.Fly;
import net.superblaubeere27.clientbase.utils.FramebufferShader;
import net.superblaubeere27.clientbase.utils.OutlineShader;
import net.superblaubeere27.clientbase.utils.RenderUtils;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

public class AntiVoid extends Module {

	public AntiVoid() {
		super("AntiVoid", "Prevent to fall into the void", ModuleCategory.WORLD);
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!this.getState()) return;
		if(checkVoid() && mc.thePlayer.fallDistance > 20) {
			mc.thePlayer.onGround = false;
			mc.thePlayer.motionY = 0.0;
		}
	}
	
	private boolean checkVoid() {
        double i = (-(mc.thePlayer.posY-1.4857625));
        boolean dangerous = true;
        while (i <= 0) {
            dangerous = mc.theWorld.getCollisionBoxes(mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX * 0.5, i, mc.thePlayer.motionZ * 0.5)).isEmpty();
            i++;
            if (!dangerous) break;
        }
        return dangerous;
    }

}
