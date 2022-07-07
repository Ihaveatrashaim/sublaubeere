/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.superblaubeere27.clientbase.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "Prevent kb", ModuleCategory.COMBAT);
    }

    private ModeValue modes = new ModeValue("Modes", "Legit", "Legit", "AACZero");
    
    public String getMode(){
        int Mode = modes.getObject();
        if(Mode == 0) return "Legit";
        if (Mode == 1) return "AACZero";

        else return null;
    }
    
    @EventTarget
    public void onMotionUpdate(MotionUpdateEvent event){
    	this.setDisplayName("Velocity" + "§7 " + getMode());
    	
    	double posX = 0;
    	double posZ =0;
    	if(!(mc.thePlayer.hurtTime > 0)) {
    		posX = mc.thePlayer.posX;
        	posZ =mc.thePlayer.posZ;
    	}
    	
    	if(getMode().equalsIgnoreCase("Legit")) {
    		if(mc.thePlayer.hurtTime > 5){
                mc.thePlayer.onGround = true;
            }
    	}
        if(getMode().equalsIgnoreCase("AACZero")) {
        	if(mc.thePlayer.hurtTime > 0) {
        		if (mc.thePlayer.onGround || mc.thePlayer.fallDistance > 2F)
                    return;

                mc.thePlayer.motionY -= 3.0F;
                mc.thePlayer.isAirBorne = true;
                mc.thePlayer.onGround = true;
            }
        }
    }
}
