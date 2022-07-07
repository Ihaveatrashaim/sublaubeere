package net.superblaubeere27.clientbase.events;

import com.darkmagician6.eventapi.events.Event;

public class SlowDownEvent implements Event{

	public float strafe;
	public float forward;
	
	public SlowDownEvent(float strafe, float forward) {
		this.strafe = strafe;
		this.forward = forward;
	}

	public float getStrafe() {
		return strafe;
	}

	public float getForward() {
		return forward;
	}

}
