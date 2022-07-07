/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.events.KeyEvent;
import net.superblaubeere27.clientbase.gui.clickgui.ClickGUI;
import net.superblaubeere27.clientbase.gui.clickgui2.VapeClickGui;
import net.superblaubeere27.clientbase.modules.modules.combat.*;
import net.superblaubeere27.clientbase.modules.modules.exploit.*;
import net.superblaubeere27.clientbase.modules.modules.movement.*;
import net.superblaubeere27.clientbase.modules.modules.render.*;
import net.superblaubeere27.clientbase.modules.modules.render.tabgui.TabGui;
import net.superblaubeere27.clientbase.modules.modules.world.*;
import net.superblaubeere27.clientbase.scripting.ScriptModule;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.Value;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    @NotNull
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        EventManager.register(this);
    }


    public void addModules() {
        addModule(new SetbackDetector());
        addModule(new AirStuck());
        addModule(new Teleport());
        addModule(new NoFall());
        addModule(new ClickGui());
        addModule(new Fly());
        addModule(new ESP());
        addModule(new AntiVoid());
        addModule(new KillAura());
        addModule(new HighJump());
        addModule(new Jesus());
        addModule(new Velocity());
        addModule(new ChestStealer());
        addModule(new Speed());
        addModule(new FakeLag());
        addModule(new ConsoleSpammer());
        addModule(new Step());
        addModule(new Freecam());
        addModule(new NoSlow());
        addModule(new FastPlace());
    	addModule(new TabGui());
        addModule(new AirJump());
        addModule(new InventoryHelper());
        addModule(new Sneak());
        addModule(new ScaffoldWalk());
        addModule(new NoRotateSet());
        addModule(new HUD());
    }

    private void addModule(@NotNull Module module) {
        modules.add(module);
        EventManager.register(module);
        ClientBase.INSTANCE.valueManager.registerObject(module.getName(), module);
    }
    
    private void removeModule(@NotNull Module module) {
        modules.remove(module);
        EventManager.unregister(module);
        ClientBase.INSTANCE.valueManager.unregisterObject(module.getName(), module);
    }


    @NotNull
    public List<Module> getModules() {
        return modules;
    }
    
    public List<Module> getModuleByCategory(ModuleCategory c){
    	List<Module> modules = new ArrayList<Module>();
    	
    	for(Module m : getModules()) {
    		if(m.getCategory() == c) {
    			modules.add(m);
    		}
    	}
		return modules;
    }

    @NotNull
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }

    public Module getModule(@NotNull String name, boolean caseSensitive) {
        return modules.stream().filter(mod -> !caseSensitive && name.equalsIgnoreCase(mod.getName()) || name.equals(mod.getName())).findFirst().orElse(null);
    }
    

    @EventTarget
    private void onKey(@NotNull KeyEvent event) {
        for (Module module : modules) if (module.getKeybind() == event.getKey()) module.setState(!module.getState());
        
        if(event.getKey() == ClientBase.INSTANCE.moduleManager.getModule("ClickGui", false).getKeybind()) {
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
        	ClientBase.INSTANCE.moduleManager.getModule("ClickGui", false).setState(false);
        }
    }

    public void addScriptModule(ScriptModule module) {
        addModule(module);
    }


	public void reloadModules() {
		addModules();
	}
}
