package com.redomar.game.event;

import com.redomar.game.Game;
import com.redomar.game.lib.Either;
import com.redomar.game.lib.SleepThread;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;
import java.util.HashMap;
import java.util.Map;

import static com.redomar.game.lib.Either.tryCatch;

public class InputHandler implements KeyListener {

	private final boolean frenchKeyboardLayout;
	private final Printer inputPrinter = new Printer(PrintTypes.INPUT);
	private final KeyHandler UP_KEY = new KeyHandler();
	private final KeyHandler DOWN_KEY = new KeyHandler();
	private final KeyHandler LEFT_KEY = new KeyHandler();
	private final KeyHandler RIGHT_KEY = new KeyHandler();
	private final KeyHandler SHIFTED = new KeyHandler();
	private final KeyHandler M_KEY = new KeyHandler();


	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		frenchKeyboardLayout = context.getLocale().getCountry().equals("BE") || context.getLocale().getCountry().equals("FR");
		game.addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	/**
	 * Toggles between two actions depending on the value of a boolean flag.
	 *
	 * @param key       the key to be pressed to trigger the action
	 * @param isToggled the boolean flag that determines which action to activate
	 * @param actionA   the function to execute the first action
	 * @param actionB   the function to execute the second action
	 * @return an Either object with the result of the action on the right side and an Exception on the left side if an exception is thrown
	 */
	public Either<Exception, Boolean> toggleActionWithCheckedRunnable(KeyHandler key, boolean isToggled, Either.CheckedRunnable actionA, Either.CheckedRunnable actionB) {
		boolean pressed = key.isPressed();
		if (pressed) {
			if (!isToggled) {
				Either<Exception, Void> result = tryCatch(actionA);
				if (result.isLeft()) {
					return result.map(__ -> false);
				}
				isToggled = true;
			} else {
				Either<Exception, Void> result = tryCatch(actionB);
				if (result.isLeft()) {
					return result.map(__ -> true);
				}
				isToggled = false;
			}
			key.reset();
		}
		return Either.right(isToggled);
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

		keyCodeActions.put(KeyEvent.VK_S, () -> DOWN_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_D, () -> RIGHT_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_UP, () -> UP_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_LEFT, () -> LEFT_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_DOWN, () -> DOWN_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_RIGHT, () -> RIGHT_KEY.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_SHIFT, () -> SHIFTED.setPressed(isPressed));
		keyCodeActions.put(KeyEvent.VK_M, () -> M_KEY.setPressedToggle(isPressed));

		if (frenchKeyboardLayout) {
			keyCodeActions.put(KeyEvent.VK_Q, () -> LEFT_KEY.setPressed(isPressed));
			keyCodeActions.put(KeyEvent.VK_Z, () -> UP_KEY.setPressed(isPressed));
			keyCodeActions.put(KeyEvent.VK_A, this::quitGame);
		} else {
			keyCodeActions.put(KeyEvent.VK_A, () -> LEFT_KEY.setPressed(isPressed));
			keyCodeActions.put(KeyEvent.VK_W, () -> UP_KEY.setPressed(isPressed));
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

	public void overWriteKey(KeyHandler key, boolean isPressed) {
		key.setPressedToggle(isPressed);
	}

	private void quitGame() {
		Game.setClosing(true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			inputPrinter.exception(e.getMessage());
		}
		Game.getLevel().removeEntity(Game.getPlayer().getName());
		Game.getGame().stop();
		Game.getFrame().dispose();
		if (!inputPrinter.removeLog()) System.err.println("Could not delete Log file");
		System.exit(0);
	}

	public KeyHandler getUP_KEY() {
		return UP_KEY;
	}

	public KeyHandler getDOWN_KEY() {
		return DOWN_KEY;
	}

	public KeyHandler getLEFT_KEY() {
		return LEFT_KEY;
	}

	public KeyHandler getRIGHT_KEY() {
		return RIGHT_KEY;
	}

	public KeyHandler getSHIFTED() {
		return SHIFTED;
	}

	public KeyHandler getM_KEY() {
		return M_KEY;
	}

}
