package com.redomar.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.redomar.game.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {

	@SuppressWarnings("unused")
	private final Game game;

	public WindowHandler(Game game) {
		this.game = game;
		Game.getFrame().addWindowListener(this);
	}

	public void windowActivated(WindowEvent event) {

	}

	public void windowClosed(WindowEvent event) {

	}

	public void windowClosing(WindowEvent event) {
		Packet01Disconnect packet = new Packet01Disconnect(Game.getPlayer()
				.getUsername());
		packet.writeData(Game.getSocketClient());
	}

	public void windowDeactivated(WindowEvent event) {

	}

	public void windowDeiconified(WindowEvent event) {

	}

	public void windowIconified(WindowEvent event) {

	}

	public void windowOpened(WindowEvent event) {

	}

}
