package com.redomar.game.event;

import com.redomar.game.Game;
import com.redomar.game.lib.Either;
import com.redomar.game.lib.SleepThread;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

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


	private Controller controller;

	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		frenchKeyboardLayout = context.getLocale().getCountry().equals("BE") || context.getLocale().getCountry().equals("FR");
		game.addKeyListener(this);

		// Detect the controller
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller c : controllers) {
			if (c.getType() == Controller.Type.GAMEPAD) {
				controller = c;
				break;
			}
		}

		if (controller == null) {
			System.out.println("No gamepad found.");
		} else {
			System.out.println("Gamepad detected: " + controller.getName());
		}
	}

	public void pollController() {
		if (controller == null) {
			return;
		}

		controller.poll();
		EventQueue queue = controller.getEventQueue();
		Event event = new Event();

		while (queue.getNextEvent(event)) {
			String componentName = event.getComponent().getName();
			float value = event.getValue();

			// Process the event
			processControllerInput(componentName, value);
		}
	}

	private void processControllerInput(String componentName, float value) {


		// Threshold to detect significant movement
		float threshold = 0.5f; // Adjusted for more responsive input

		// if the value is lower than -0.5 or higer that 0.5 print whcih key is pressed
		if (value <= -0.5 || value >= 0.5) {
			System.out.println(componentName + " " + value);
		}
		switch (componentName) {


			case "x": // Horizontal movement (left/right)
				if (value < -threshold) {
					LEFT_KEY.setPressed(true);
				} else if (value > threshold) {
					RIGHT_KEY.setPressed(true);
				} else {
					LEFT_KEY.setPressed(false);
					RIGHT_KEY.setPressed(false);
				}
				break;

			case "y": // Vertical movement (up/down)
				if (value < -threshold) {
					UP_KEY.setPressed(true);
				} else if (value > threshold) {
					DOWN_KEY.setPressed(true);
				} else {
					UP_KEY.setPressed(false);
					DOWN_KEY.setPressed(false);
				}
				break;

			// Handle other components (e.g., buttons or additional axes) as needed
			case "ry":
				if (value > -threshold) {
					SHIFTED.setPressed(true);
				} else {
					SHIFTED.setPressed(false);
				}
				break;

			case "0":
				if (value == 1) {
					Game.setChangeLevel(true);
				}
				break;

			case "4":
				if (value == 1) {
					if (!Game.isNpc()) {
						Game.setNpc(true);
						Game.npcSpawn();
						inputPrinter.print("Dummy has been spawned", PrintTypes.GAME);
					}
				}
				break;


			case "5": // toggle K key
				if (value == 1) {
					if (Game.isNpc()) {
						Game.setNpc(false);
						Game.npcKill();
						inputPrinter.print("Dummy has been removed", PrintTypes.GAME);
					}
				}
				break;



			case "9": // toggle M key
				if (value == 1) {
					M_KEY.setPressedToggle(M_KEY.isPressed());
				}
				break;


			case "13":
				if (value == 1) {
					if (!Game.isClosing()) {
						Game.setDevMode(!Game.isDevMode());
						new Thread(new SleepThread());
						inputPrinter.print(String.format("Debug Mode %s", Game.isDevMode() ? "Enabled" : "Disabled"));
					}
				}
				break;
			case "8":
				if (value == 1) {
					this.quitGame();
				}

		}
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
