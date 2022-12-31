package com.redomar.game;

import com.redomar.game.lib.SleepThread;
import com.redomar.game.script.PopUp;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;

public class InputHandler implements KeyListener {

	private final boolean isAzertyCountry;
	private final Printing print = new Printing();
	private final PopUp popup = new PopUp();
	private final Key UP_KEY = new Key();
	private final Key DOWN_KEY = new Key();
	private final Key LEFT_KEY = new Key();
	private final Key RIGHT_KEY = new Key();
	private boolean ignoreInput = false;
	private boolean toggleMusic = false;

	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		// Important to know whether the keyboard is in Azerty or Qwerty.
		// Azerty countries used QZSD instead of WASD keys.
		isAzertyCountry = context.getLocale().getCountry().equals("BE") || context.getLocale().getCountry().equals("FR");
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
			if (keyCode == KeyEvent.VK_Z && isAzertyCountry || keyCode == KeyEvent.VK_W && !isAzertyCountry || keyCode == KeyEvent.VK_UP) {
				UP_KEY.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_Q && isAzertyCountry || keyCode == KeyEvent.VK_A && !isAzertyCountry || keyCode == KeyEvent.VK_LEFT) {
				LEFT_KEY.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
				DOWN_KEY.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
				RIGHT_KEY.toggle(isPressed);
			}
		}
		if (isIgnoreInput()) {
			UP_KEY.toggle(false);
			DOWN_KEY.toggle(false);
			LEFT_KEY.toggle(false);
			RIGHT_KEY.toggle(false);
		}

		if (keyCode == KeyEvent.VK_M) {
			if (!toggleMusic) {
				Game.getBackgroundMusic().play();
				print.print("Playing Music", PrintTypes.MUSIC);
				toggleMusic = true;
			}
		}

		if (keyCode == KeyEvent.VK_COMMA) {
			Game.getBackgroundMusic().stop();
			if (toggleMusic) print.print("Stopping Music", PrintTypes.MUSIC);
			toggleMusic = false;
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
		if (!print.removeLog()) System.err.println("Could not delete Log file");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Game.getLevel().removeEntity(Game.getPlayer().getSanitisedUsername());
		Game.setRunning(false);
		Game.getFrame().dispose();
		System.exit(0);
	}

	public void untoggle(boolean toggle) {
		this.ignoreInput = toggle;
	}

	public Key getUP_KEY() {
		return UP_KEY;
	}

	public Key getDOWN_KEY() {
		return DOWN_KEY;
	}

	public Key getLEFT_KEY() {
		return LEFT_KEY;
	}

	public Key getRIGHT_KEY() {
		return RIGHT_KEY;
	}

	public boolean isIgnoreInput() {
		return ignoreInput;
	}

	private void setIgnoreInput(boolean ignoreInput) {
		this.ignoreInput = ignoreInput;
	}

	public static class Key {
		private int numTimesPressed = 0;
		private boolean pressed = false;

		@Deprecated
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

		@Deprecated
		public void off() {
			pressed = false;
			numTimesPressed = 0;
		}
	}

}
