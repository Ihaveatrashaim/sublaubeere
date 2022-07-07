package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;

import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;

public class HighJump extends Module{

	
	
	public HighJump() {
		super("HighJump", "Make you jump higher", ModuleCategory.MOVEMENT);
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
	}

}
