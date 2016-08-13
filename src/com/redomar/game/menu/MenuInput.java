package com.redomar.game.menu;

import com.redomar.game.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuInput implements KeyListener {

	private boolean ticket = false;

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {

	}

	private void toggleKey(int keyCode) {

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			Menu.setSelectedStart(true);
			Menu.setSelectedExit(false);
		}

		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			Menu.setSelectedExit(true);
			Menu.setSelectedStart(false);
		}

		if(!ticket){
			if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
				if (Menu.isSelectedStart()) {
					this.ticket = true;
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
					new Game().start();
				}

				if (Menu.isSelectedExit()) {
					this.ticket = true;
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
				}
			}
		}

		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
	}

}
