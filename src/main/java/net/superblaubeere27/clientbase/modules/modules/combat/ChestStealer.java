package net.superblaubeere27.clientbase.modules.modules.combat;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.darkmagician6.eventapi.EventTarget;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;

public class ChestStealer extends Module {

	public ChestStealer() {
		super("ChestStealer", "Steal item in chest", ModuleCategory.COMBAT);
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!this.getState()) return;
        ArrayList<ItemStack> set = new ArrayList<>();
        ContainerChest cc = (ContainerChest) mc.thePlayer.openContainer;
        TimeHelper time = new TimeHelper();
        if(cc == null) return;
        for(int i = 0; i<cc.getLowerChestInventory().getSizeInventory(); i++)
        {
        if(time.hasReached(100L))
        {
        set.add(cc.inventorySlots.get(i).getStack());
        if((cc.getLowerChestInventory().getStackInSlot(i) != null))
        {
        this.mc.playerController.windowClick(cc.windowId, i, 0, 1, this.mc.thePlayer);

        time.reset();
        continue;
        }
        }
        }
        if(set.size() >= cc.getInventory().size()-36)
        {
        if(StreamSupport.stream(set.spliterator(), true).allMatch(o -> o == null)) {
        mc.thePlayer.closeScreen();
        //mc.displayGuiScreen(null);
        }
        }
	}


}
