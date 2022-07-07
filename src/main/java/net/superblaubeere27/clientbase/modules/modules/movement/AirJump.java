package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", "Jumping in mid air", ModuleCategory.MOVEMENT);
	}
	
    private ModeValue modes = new ModeValue("Modes", "Vanilla", "Vanilla", "AAC");
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!getState()) return;
		
		TimeHelper timer = new TimeHelper();
		
    	this.setDisplayName("AirJump" + "§7 " + getMode());
		
		//Vanilla
		if(modes.getObject() == 0) {
			if(mc.gameSettings.keyBindJump.isPressed()) {
				mc.thePlayer.jump();
			}
		}
		
		//AAC
		if(modes.getObject() == 1) {
			if(!mc.thePlayer.onGround) {
				if(!timer.hasReached(60)) {
					mc.thePlayer.motionY += 0.059;
				}else {
					mc.thePlayer.onGround = true;
				}
			}
		}
		
		
	}

	private String getMode() {
		if(modes.getObject() == 0) return "Vanilla";
		if(modes.getObject() == 1) return "AAC";
		else return null;
	}

}
