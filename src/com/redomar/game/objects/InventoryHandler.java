package com.redomar.game.objects;

import com.redomar.game.menu.DedicatedJFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class InventoryHandler implements WindowListener {

	@SuppressWarnings("unused")
	private DedicatedJFrame frame;

	public InventoryHandler(DedicatedJFrame frame) {
		this.frame = frame;
		DedicatedJFrame.getFrameStatic().addWindowListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {
		Inventory.closing = true;
		System.out.println("CLOSING");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

}
