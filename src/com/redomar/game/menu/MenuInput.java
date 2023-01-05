package com.redomar.game.menu;

import com.redomar.game.Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuInput implements KeyListener {

	private static final HelpMenu HELP_MENU = new HelpMenu();
	private boolean ticket = false;
	private boolean help = false;

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

		if (!ticket) {
			if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
				if (Menu.isSelectedStart()) {
					this.ticket = true;
					new Game().start();
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
					Menu.stop();
				}

				if (Menu.isSelectedExit()) {
					this.ticket = true;
					Menu.setRunning(false);
					Menu.getFrame().setVisible(false);
					Menu.getFrame().stopFrame();
					Menu.stop();
				}
			}
		}

		if (keyCode == KeyEvent.VK_ESCAPE) {
			HELP_MENU.helpMenuClose();
			help = false;
			System.exit(0);
		}

		if (keyCode == KeyEvent.VK_H) {
			if (!help) HELP_MENU.helpMenuLaunch();
			help = true;
		}
	}

}
