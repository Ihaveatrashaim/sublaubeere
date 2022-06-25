package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

public class Step extends Module{

    private ModeValue modes = new ModeValue("Fly modes", "Normal", "Normal","Reverse");
	private float step;
    
	public Step() {
		super("Step", "Step higher", ModuleCategory.MOVEMENT);
	}
	
	@Override
	public void onEnable() {
		step = mc.thePlayer.stepHeight;
	}
	
	@Override
	public void onDisable() {
		if(mc.thePlayer == null) return;
		
		mc.thePlayer.stepHeight = step;
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!getState()) return;
		
		if(modes.getObject() == 0) {
			this.setName("Step§7 Normal");
			mc.thePlayer.stepHeight = 1.2F;
		}
		
		if(modes.getObject() == 1) {
			this.setName("Step§7 Reverse");
			
			mc.thePlayer.stepHeight = -1.2F;

		}
	}

}
