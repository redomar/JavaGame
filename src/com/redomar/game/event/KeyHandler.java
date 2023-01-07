package com.redomar.game.event;

@SuppressWarnings("unused")
public class KeyHandler {
	private int numTimesPressed = 0;
	private boolean pressed = false;

	@Deprecated
	public int getNumTimesPressed() {
		return numTimesPressed;
	}

	public boolean isPressed() {
		return pressed;
	}

	void setPressed(boolean isPressed) {
		pressed = isPressed;
		if (isPressed) numTimesPressed++;
	}

	void setPressedToggle(boolean isPressed) {
		if (isPressed) return;
		pressed = !pressed;
	}

	public void on() {
		pressed = true;
	}

	public void reset() {
		pressed = false;
		numTimesPressed = 0;
	}
}
