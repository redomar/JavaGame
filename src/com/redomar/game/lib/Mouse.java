package com.redomar.game.lib;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener{

	public void mouseDragged(MouseEvent e) {
		System.out.println("Dragged");
	}

	public void mouseMoved(MouseEvent e) {
		System.out.println("Moved");
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println("Clicked");
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println("Enter");
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("Exit");
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("Press");
	}

	public void mouseReleased(MouseEvent e) {
		System.out.println("Release");
	}

}
