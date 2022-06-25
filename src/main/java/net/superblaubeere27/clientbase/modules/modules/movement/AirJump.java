package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", "Jumping in mid air", ModuleCategory.MOVEMENT);
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!getState()) return;
		
		if(mc.gameSettings.keyBindJump.isPressed()) {
			mc.thePlayer.jump();
		}
		
	}

}
