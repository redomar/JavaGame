package com.redomar.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseListener, MouseMotionListener{

	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;

	public MouseHandler(Game game){
		game.addMouseListener(this);
		game.addMouseMotionListener(this);
	}

	public int getX() {
		return mouseX;
	}

	public int getY() {
		return mouseY;
	}

	public int getButton() {
		return mouseB;
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = -1;
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

}
