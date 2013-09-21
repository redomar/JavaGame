package com.redomar.game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.redomar.game.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener{

	private final Game game;
	
	public WindowHandler(Game game){
		this.game = game;
		this.game.getFrame().addWindowListener(this);
	}
	
	@Override
	public void windowActivated(WindowEvent event) {
		
	}

	@Override
	public void windowClosed(WindowEvent event) {
		
	}

	@Override
	public void windowClosing(WindowEvent event) {
		Packet01Disconnect packet = new Packet01Disconnect(Game.getPlayer().getUsername());
		packet.writeData(this.game.getSocketClient());
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
