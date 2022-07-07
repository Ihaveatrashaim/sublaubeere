package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.TimeHelper;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;

public class Step extends Module{

    private ModeValue modes = new ModeValue("Step modes", "Normal", "Normal","Reverse");
	private float step;
    
	public Step() {
		super("Step", "Step higher", ModuleCategory.MOVEMENT);
		
	}
	
	@Override
	public void onEnable() {
		if(mc.thePlayer == null) return;
		
		step = mc.thePlayer.stepHeight;
	}
	
	@Override
	public void onDisable() {
		if(mc.thePlayer == null) return;
		
		mc.gameSettings.keyBindJump.setKeyBindState(57, false);
		mc.thePlayer.stepHeight = step;
	}
	
	@EventTarget
	public void onMotion(MotionUpdateEvent e) {
		if(!getState()) return;
		
		TimeHelper timer = new TimeHelper();
		
		mc.thePlayer.stepHeight = step;
		
		if(modes.getObject() == 0) {						
			if(mc.thePlayer.isCollidedHorizontally) {
				mc.thePlayer.jump();
				
				if(!mc.thePlayer.onGround) {
					mc.thePlayer.motionY += 0.059;
					mc.thePlayer.onGround = true;
				}
			}else {
			}
			

		}
		
		if(modes.getObject() == 1) {
			if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.onGround && !mc.thePlayer.movementInput.jump && mc.thePlayer.motionY <= 0.0 && mc.thePlayer.fallDistance <= 1f && !
					mc.gameSettings.keyBindJump.isKeyDown())
	            mc.thePlayer.motionY = (-1f);

		}
	}

}
