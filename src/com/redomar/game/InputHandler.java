package com.redomar.game;

import com.redomar.game.lib.SleepThread;
import com.redomar.game.script.PopUp;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;

public class InputHandler implements KeyListener {

	private boolean isAzertyCountry;
	private Key up = new Key();
	private Key down = new Key();
	private Key left = new Key();
	private Key right = new Key();
	private Printing print = new Printing();
	private int map;
	private boolean ignoreInput = false;
	private PopUp popup = new PopUp();

	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		// Important to know whether the keyboard is in Azerty or Qwerty.
		// Azerty countries used QZSD instead of WASD keys.
		isAzertyCountry = context.getLocale().getCountry().equals("BE")
				|| context.getLocale().getCountry().equals("FR");
		game.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {

	}

	private void toggleKey(int keyCode, boolean isPressed) {
		if (!isIgnoreInput()) {
			if (keyCode == KeyEvent.VK_Z && isAzertyCountry || keyCode == KeyEvent.VK_W && !isAzertyCountry
					|| keyCode == KeyEvent.VK_UP) {
				up.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_Q && isAzertyCountry || keyCode == KeyEvent.VK_A && !isAzertyCountry
					|| keyCode == KeyEvent.VK_LEFT) {
				left.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
				down.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
				right.toggle(isPressed);
			}
		}
		if (isIgnoreInput()) {
			up.toggle(false);
			down.toggle(false);
			left.toggle(false);
			right.toggle(false);
		}

		if (keyCode == KeyEvent.VK_M) {
			Game.getBackgroundMusic().play();
		}

		if (keyCode == KeyEvent.VK_COMMA) {
			Game.getBackgroundMusic().stop();
		}


		if (keyCode == KeyEvent.VK_W && isAzertyCountry || keyCode == KeyEvent.VK_Z && !isAzertyCountry) {
			// if (map == 0){
			// Game.getGame().setMap("/levels/water_level.png");
			// map++;
			// } else{
			// Game.getGame().setMap("/levels/custom_level.png");
			// map--;
			// }
			if (Game.getMap() == 2) {
				if (Game.isNpc()) {
					Game.setNpc(false);
				}
				Game.setChangeLevel(true);
			}
		}
		if (keyCode == KeyEvent.VK_N) {
			if (Game.getPlayer().isMoving()) {
				setIgnoreInput(true);
				int n = popup.Warn("Stop moving before spawning dummy AI");
				if (n == 0) {
					setIgnoreInput(false);
				}
				return;
			}
			if (!Game.isNpc()) {
				Game.setNpc(true);
				Game.npcSpawn();
				print.print("Dummy has been spawned", PrintTypes.GAME);
			}
		}
		if (keyCode == KeyEvent.VK_K) {
			if (Game.isNpc()) {
				Game.setNpc(false);
				Game.npcKill();
				print.print("Dummy has been removed", PrintTypes.GAME);
			}
		}

		if (keyCode == KeyEvent.VK_A && isAzertyCountry || keyCode == KeyEvent.VK_Q && !isAzertyCountry)
			this.quitGame();

		if (keyCode == KeyEvent.VK_BACK_QUOTE) {
			if (!Game.isClosing() && !Game.isDevMode()) {
				Game.setDevMode(true);
				new Thread(new SleepThread());
			}
		}
	}

	private void quitGame() {
		Game.setClosing(true);
		print.removeLog();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Game.getLevel().removeEntity(
				Game.getPlayer().getSanitisedUsername());
		Game.setRunning(false);
		Game.getFrame().dispose();
		System.exit(1);
	}

	public void untoggle(boolean toggle) {
		this.ignoreInput = toggle;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	public Key getUp() {
		return up;
	}

	public void setUp(Key up) {
		this.up = up;
	}

	public Key getDown() {
		return down;
	}

	public void setDown(Key down) {
		this.down = down;
	}

	public Key getLeft() {
		return left;
	}

	public void setLeft(Key left) {
		this.left = left;
	}

	public Key getRight() {
		return right;
	}

	public void setRight(Key right) {
		this.right = right;
	}

	public boolean isIgnoreInput() {
		return ignoreInput;
	}

	private void setIgnoreInput(boolean ignoreInput) {
		this.ignoreInput = ignoreInput;
	}

	public class Key {
		private int numTimesPressed = 0;
		private boolean pressed = false;

		public int getNumTimesPressed() {
			return numTimesPressed;
		}

		public boolean isPressed() {
			return pressed;
		}

		void toggle(boolean isPressed) {
			pressed = isPressed;
			if (isPressed) {
				numTimesPressed++;
			}
		}

		public void off() {
			pressed = false;
			numTimesPressed = 0;
		}
	}

}
