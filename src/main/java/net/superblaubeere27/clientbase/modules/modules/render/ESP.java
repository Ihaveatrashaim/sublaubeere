package net.superblaubeere27.clientbase.modules.modules.render;


import java.awt.Color;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.FramebufferShader;
import net.superblaubeere27.clientbase.utils.OutlineShader;
import net.superblaubeere27.clientbase.utils.RenderUtils;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

public class ESP extends Module {

	public ESP() {
		super("ESP", "Show players around you", ModuleCategory.RENDER);
	}
	
	private static Timer timer = new Timer(20.0F);
    private NumberValue<Integer> red = new NumberValue<Integer>("Red", 0, 0, 255);
    private NumberValue<Integer> green = new NumberValue<Integer>("Green", 0, 0, 255);
    private NumberValue<Integer> blue = new NumberValue<Integer>("Blue", 0, 0, 255);
	private boolean renderNameTags = true;
	
	@EventTarget
	public void render2D(Render2DEvent e) {
		
	}

}
