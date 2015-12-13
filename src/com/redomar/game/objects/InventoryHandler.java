package com.redomar.game.objects;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.redomar.game.menu.DedicatedJFrame;

public class InventoryHandler implements WindowListener {
	
	@SuppressWarnings("unused")
	private DedicatedJFrame frame;
	
	public InventoryHandler(DedicatedJFrame frame) {
		this.frame = frame;
		DedicatedJFrame.getFrameStatic().addWindowListener(this);
	}

	public void windowActivated(WindowEvent e) {
		
	}

	public void windowClosed(WindowEvent e) {
		
	}

	public void windowClosing(WindowEvent e) {
		Inventory.closing = true;
		System.out.println("CLOSING");
	}


	public void windowDeactivated(WindowEvent e) {
		
	}


	public void windowDeiconified(WindowEvent e) {
		
	}


	public void windowIconified(WindowEvent e) {
		
	}


	public void windowOpened(WindowEvent e) {
		
	}

}
