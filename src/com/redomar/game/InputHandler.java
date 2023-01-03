package com.redomar.game;

import com.redomar.game.lib.SleepThread;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener {

	private final boolean frenchKeyboardLayout;
	private final Printer inputPrinter = new Printer(PrintTypes.INPUT);
	private final Key UP_KEY = new Key();
	private final Key DOWN_KEY = new Key();
	private final Key LEFT_KEY = new Key();
	private final Key RIGHT_KEY = new Key();
	private boolean musicPlaying = false;


	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		frenchKeyboardLayout = context.getLocale().getCountry().equals("BE") || context.getLocale().getCountry().equals("FR");
		game.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		toggleKey(keyCode, false);
		if (keyCode == KeyEvent.VK_BACK_QUOTE) {
			if (!Game.isClosing()) {
				Game.setDevMode(!Game.isDevMode());
				new Thread(new SleepThread());
				inputPrinter.print(String.format("Debug Mode %s", Game.isDevMode() ? "Enabled" : "Disabled"));
			}
		}

		if (keyCode == KeyEvent.VK_M) {
			if (!musicPlaying) {
				Game.getBackgroundMusic().play();
				musicPlaying = true;
			} else {
				Game.getBackgroundMusic().stop();
				musicPlaying = false;
			}
		}

		if (keyCode == KeyEvent.VK_N) {
			if (!Game.isNpc()) {
				Game.setNpc(true);
				Game.npcSpawn();
				inputPrinter.print("Dummy has been spawned", PrintTypes.GAME);
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	private void toggleKey(int keyCode, boolean isPressed) {
		Map<Integer, Runnable> keyCodeActions = new HashMap<>();

		keyCodeActions.put(KeyEvent.VK_S, () -> DOWN_KEY.toggle(isPressed));
		keyCodeActions.put(KeyEvent.VK_D, () -> RIGHT_KEY.toggle(isPressed));
		keyCodeActions.put(KeyEvent.VK_UP, () -> UP_KEY.toggle(isPressed));
		keyCodeActions.put(KeyEvent.VK_LEFT, () -> LEFT_KEY.toggle(isPressed));
		keyCodeActions.put(KeyEvent.VK_DOWN, () -> DOWN_KEY.toggle(isPressed));
		keyCodeActions.put(KeyEvent.VK_RIGHT, () -> RIGHT_KEY.toggle(isPressed));

		if (frenchKeyboardLayout) {
			keyCodeActions.put(KeyEvent.VK_Q, () -> LEFT_KEY.toggle(isPressed));
			keyCodeActions.put(KeyEvent.VK_Z, () -> UP_KEY.toggle(isPressed));
			keyCodeActions.put(KeyEvent.VK_A, this::quitGame);
		} else {
			keyCodeActions.put(KeyEvent.VK_A, () -> LEFT_KEY.toggle(isPressed));
			keyCodeActions.put(KeyEvent.VK_W, () -> UP_KEY.toggle(isPressed));
			keyCodeActions.put(KeyEvent.VK_Q, this::quitGame);
		}

		if (keyCodeActions.containsKey(keyCode)) keyCodeActions.get(keyCode).run();

		if (keyCode == KeyEvent.VK_W && frenchKeyboardLayout || keyCode == KeyEvent.VK_Z && !frenchKeyboardLayout) {
			if (Game.getMap() == 2) {
				if (Game.isNpc()) {
					Game.setNpc(false);
				}
				Game.setChangeLevel(true);
			}
		}

		if (keyCode == KeyEvent.VK_K) {
			if (Game.isNpc()) {
				Game.setNpc(false);
				Game.npcKill();
				inputPrinter.print("Dummy has been removed", PrintTypes.GAME);
			}
		}

	}

	private void quitGame() {
		Game.setClosing(true);
		if (!inputPrinter.removeLog()) System.err.println("Could not delete Log file");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Game.getLevel().removeEntity(Game.getPlayer().getName());
		Game.getGame().stop();
		Game.getFrame().dispose();
		System.exit(0);
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
