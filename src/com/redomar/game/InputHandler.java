package com.redomar.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.im.InputContext;

import com.redomar.game.lib.SleepThread;
import com.redomar.game.script.PopUp;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;

public class InputHandler implements KeyListener {

	private boolean isAzertyCountry;

	public InputHandler(Game game) {
		InputContext context = InputContext.getInstance();
		// Important to know wether the keyboard is in Azerty or Qwerty.
		// Azerty countries used QZSD instead of WASD keys.
		isAzertyCountry = context.getLocale().getCountry().equals("BE")
				|| context.getLocale().getCountry().equals("FR");
		game.addKeyListener(this);
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

		public void toggle(boolean isPressed) {
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

	private Key up = new Key();
	private Key down = new Key();
	private Key left = new Key();
	private Key right = new Key();
	private Printing print = new Printing();
	private boolean PlayMusic = false;
	private int map;
	private boolean ignoreInput = false;
	private PopUp popup = new PopUp();

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void toggleKey(int keyCode, boolean isPressed) {
		if (isIgnoreInput() == false) {
			if (isAzertyCountry) {
				if (keyCode == KeyEvent.VK_Z || keyCode == KeyEvent.VK_UP) {
					up.toggle(isPressed);
				}

				if (keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_LEFT) {
					left.toggle(isPressed);
				}
			} else {
				if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
					up.toggle(isPressed);
				}

				if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
					left.toggle(isPressed);
				}
			}

			if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
				down.toggle(isPressed);
			}

			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
				right.toggle(isPressed);
			}
		}
		if (isIgnoreInput() == true) {
			up.toggle(false);
			down.toggle(false);
			left.toggle(false);
			right.toggle(false);
		}
		if (keyCode == KeyEvent.VK_M) {
			this.setPlayMusic(true);
		}

		if (isAzertyCountry) {
			if (keyCode == KeyEvent.VK_W) {
				// if (map == 0){
				// Game.getGame().setMap("/levels/water_level.png");
				// map++;
				// } else{
				// Game.getGame().setMap("/levels/custom_level.png");
				// map--;
				// }
				if (Game.getMap() == 2) {
					Game.setChangeLevel(true);
					Game.setNpc(false);
				}
			}
		} else {
			if (keyCode == KeyEvent.VK_Z) {
				// if (map == 0){
				// Game.getGame().setMap("/levels/water_level.png");
				// map++;
				// } else{
				// Game.getGame().setMap("/levels/custom_level.png");
				// map--;
				// }
				if (Game.getMap() == 2) {
					Game.setChangeLevel(true);
					Game.setNpc(false);
				}
			}
		}
		if (keyCode == KeyEvent.VK_N) {
			if (Game.getPlayer().isMoving()) {
				setIgnoreInput(true);
				int n = popup.Warn("Stop moving before spawing dummy AI");
				if (n == 0) {
					setIgnoreInput(false);
				}
				return;
			}
			if (Game.isNpc() == false) {
				Game.setNpc(true);
				Game.npcSpawn();
				print.print("Dummy has been spawned", PrintTypes.GAME);
			}
		}
		if (keyCode == KeyEvent.VK_K) {
			if (Game.isNpc() == true) {
				Game.setNpc(false);
				Game.npcKill();
				print.print("Dummy has been despawned", PrintTypes.GAME);
			}
		}
		if (isAzertyCountry) {
			if (keyCode == KeyEvent.VK_A) {
				Game.setClosing(true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Game.getLevel().removeEntity(
						Game.getPlayer().getSantizedUsername());
				Game.setRunning(false);
				Game.getFrame().dispose();
				System.exit(1);
			}
		} else {
			if (keyCode == KeyEvent.VK_Q) {
				Game.setClosing(true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Game.getLevel().removeEntity(
						Game.getPlayer().getSantizedUsername());
				Game.setRunning(false);
				Game.getFrame().dispose();
				System.exit(1);
			}
		}

		if (keyCode == KeyEvent.VK_BACK_QUOTE) {
			if (Game.isClosing() == false && Game.isDevMode() == false) {
				Game.setDevMode(true);
				new Thread(new SleepThread());
			}
		}
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

	public boolean isPlayMusic() {
		return PlayMusic;
	}

	public void setPlayMusic(boolean playMusic) {
		PlayMusic = playMusic;
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

	public void setIgnoreInput(boolean ignoreInput) {
		this.ignoreInput = ignoreInput;
	}

}
