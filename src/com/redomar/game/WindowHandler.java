package com.redomar.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowHandler implements WindowListener {

	@SuppressWarnings("unused")
	private final Game game;

	public WindowHandler(Game game) {
		this.game = game;
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
