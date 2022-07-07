/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.render.tabgui;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.events.KeyEvent;
import net.superblaubeere27.clientbase.events.Render2DEvent;
import net.superblaubeere27.clientbase.gui.clickgui.ClickGUI;
import net.superblaubeere27.clientbase.gui.clickgui2.VapeClickGui;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.modules.ModuleManager;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.utils.GLUtil;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import net.superblaubeere27.clientbase.valuesystem.Value;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TabGui extends Module {

	private Color color = new Color(0,0,0,42);
	private static int tab,moduleIndex;
	private static boolean expanded = false;
	private int justToggle = 0;
	public TimeHelper timer = new TimeHelper();
	
    public TabGui() {
        super("TabGui", "ClickGui but cannot click lol", ModuleCategory.RENDER, true, false, Keyboard.KEY_NONE);
        setState(true); 
    }
    
    @Override
    public void onEnable() {
    	tab = 0;
    	moduleIndex = 0;
    	expanded = false;
    }
    
    public int getColorToggle(boolean toggle) {
    	if(!toggle) return new Color(113,113,113, 78).getRGB();
    	else return -1;
    }
    
    @EventTarget
    private void render2D(Render2DEvent event) {
        if (!getState()) return;
    	List<Module> mod = ClientBase.INSTANCE.moduleManager.getModuleByCategory(ModuleCategory.values()[tab]);
        int Modcount = 0;
        //--------------Key event (use this for fix bug)-------------------
    	if(timer.hasReached(125)) {
    		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
        		if(expanded) {
        			if(moduleIndex <= 0) {
    					moduleIndex = mod.size() - 1;
            		}else {
            			moduleIndex--;
            		}
    			}else {
    				if(tab <= 0) {
            			tab = ModuleCategory.values().length - 1;
            		}else {
            			tab--;
            		}
    			}
        	}
        	if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
        		if(!expanded) {
    				if(tab >= ModuleCategory.values().length - 1) {
            			tab = 0;
            			
            		}else {
            			tab++;
            		}
    			}else {
    				if(moduleIndex >= mod.size() - 1) {
    					moduleIndex = 0 ;
            		}else {
            			moduleIndex++;
            		}
    				
    			}
        	}
        	if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
        		if(mod.isEmpty()) return;
    			if(expanded) {
    				if(mod.get(moduleIndex).getName().equalsIgnoreCase("ClickGui")) {
    					for(Value v :ClientBase.INSTANCE.valueManager.getAllValuesFrom("ClickGui")) {
    		        		if(v instanceof ModeValue) {
    		            		if(((ModeValue)v).getObject() == 1) {
    		                        Minecraft.getMinecraft().displayGuiScreen(new VapeClickGui());
    		            		}
    		            		
    		            		if(((ModeValue)v).getObject() == 0) {
    		                        Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
    		            		}
    		        		}
    		        	}
    				}else {
    					if(mod.get(moduleIndex).getState())
            				mod.get(moduleIndex).setState(false);
        				else
            				mod.get(moduleIndex).setState(true);
    				}
    				
    			}else {
        			expanded = true;
    			}
        	}
        	if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
    			expanded = false;
    			Modcount = 0;
        	}
        	timer.reset();
    	}
    	
    	
    	//----------------------------------------------------------------------
        FontRenderer fr = mc.fontRendererObj;
        int count = 0;

        if(expanded) {
        	Modcount = 0;
            Gui.drawRect(70, 33 + moduleIndex * 16, 160, 33 + moduleIndex * 16 + 12, 0xff0090ff);      
            Gui.drawRect(70, (int) 30.5, 165, (int) (30 + (mod.size()) * 16 + 1.5), color.getRGB());
        	for(Module m : mod) {
        		fr.drawString(m.getName(), 73, 35 + Modcount * 16, getColorToggle(m.getState()));

        		
        		Modcount++;
        	}
        }
        
        
        Gui.drawRect(5, (int) 30.5, 70, (int) (30 + (ModuleCategory.values().length) * 16 + 1.5), color.getRGB());
        Gui.drawRect(7, 33 + tab * 16, 7 + 61, 33 + tab * 16 + 12, 0xff0090ff);

        if(moduleIndex > mod.size()) {
        	moduleIndex = 0;
        }
        
        for(ModuleCategory c : ModuleCategory.values()) {
        	fr.drawString(c.name(), 10, 36 + count * 16, -1);
        	
        	count++;
        }
        
        
        
        
    }

    @EventTarget
    public void onKey(KeyEvent event) {
    	
    }
}
