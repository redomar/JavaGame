package com.redomar.game.event;

import com.redomar.game.Game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Deprecated
@SuppressWarnings("unused")
public class WindowHandler implements WindowListener {

	public WindowHandler(Game game) {
		Game.getFrame().addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent event) {

	}

	@Override
	public void windowClosed(WindowEvent event) {

	}

	@Override
	public void windowClosing(WindowEvent event) {

	}

	@Override
	public void windowDeactivated(WindowEvent event) {

	}

	@Override
	public void windowDeiconified(WindowEvent event) {

	}

	@Override
	public void windowIconified(WindowEvent event) {

	}

	@Override
	public void windowOpened(WindowEvent event) {

	}

}
