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

import net.minecraft.client.Minecraft;
import net.superblaubeere27.clientbase.ClientBase;
import net.superblaubeere27.clientbase.notifications.Notification;
import net.superblaubeere27.clientbase.notifications.NotificationManager;
import net.superblaubeere27.clientbase.notifications.NotificationType;
import net.superblaubeere27.clientbase.valuesystem.Value;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventManager;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private String DisplayName;
    private String description;
    private ModuleCategory category;
    private boolean canBeEnabled;
    private boolean hidden;
    private int keybind;
    private boolean state;
	public int optionAnim;
	public int optionAnimNow;
	private List<Value> values;

    protected Module(String name, String description, ModuleCategory moduleCategory) {
        this(name, description, moduleCategory, true, false, Keyboard.KEY_NONE);
    }

    protected Module(String name, String description, ModuleCategory category, boolean canBeEnabled, boolean hidden, int keybind) {
        this.name = name;
        this.DisplayName = name;
        this.description = description;
        this.category = category;
        this.canBeEnabled = canBeEnabled;
        this.hidden = hidden;
        this.keybind = keybind;
    }

    public List<Value> values() {
    	return ClientBase.INSTANCE.valueManager.getAllValuesFrom(this.getName());
    }
    
     protected Module(String name, String des, ModuleCategory c, int keybind) {
         this(name, des, c, true, false, keybind);
	}

	public String getName() {
        return name;
    }
	
	public void setDisplayName(String NewName) {
		this.DisplayName = NewName;
	}

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }
    
   

    public boolean isCanBeEnabled() {
        return canBeEnabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public void setName(String name) {
    	this.name = name;
    }
    
    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        if (state) {
            this.state = true;

            onEnable();

            NotificationManager.show(new Notification(NotificationType.INFO, getName(), getName() + " was enabled", 1));
            
            EventManager.register(this);

        } else {
            this.state = false;
            onDisable();

            NotificationManager.show(new Notification(NotificationType.INFO, getName(), getName() + " was disabled", 1));
            
            EventManager.unregister(this);
        }
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

	public String getDisplayName() {
		return DisplayName;
	}
}
