package com.redomar.game.event;

import com.redomar.game.Game;
import com.redomar.game.menu.Menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		if (e.getX() > 35 && e.getX() < 455) {
			// START is being selected
			Menu.setSelectedStart(e.getY() > 38 && e.getY() < 150);
			// EXIT is being selected
			Menu.setSelectedExit(e.getY() > 170 && e.getY() < 280);
		} else {
			Menu.setSelectedStart(false);
			Menu.setSelectedExit(false);
		}

	}

	public void mouseClicked(MouseEvent e) {
		if (Menu.isRunning()) {
			if (e.getX() > 35 && e.getX() < 455) {
				// START game
				if (e.getY() > 38 && e.getY() < 150) {
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
					new Game().start();
				}
				// EXIT game
				if (e.getY() > 170 && e.getY() < 280) {
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

}
