package com.redomar.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	public InputHandler(Game game) {
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
	}

	private Key up = new Key();
	private Key down = new Key();
	private Key left = new Key();
	private Key right = new Key();
	private boolean PlayMusic = false;
	private int map;

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {

	}

	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			getUp().toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			getDown().toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			getLeft().toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			getRight().toggle(isPressed);
		}
		if (keyCode == KeyEvent.VK_M) {
			this.setPlayMusic(true);
		}
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
				Game.getLevel().removeEntity(Game.getDummy());
				Game.setNpc(false);
			}
		}
		if (keyCode == KeyEvent.VK_N) {
			if (Game.isNpc() == false) {
				Game.setNpc(true);
				Game.npcSpawn();
				System.out.println("[GAME] Dummy has been spawned");
			}
		}
		if (keyCode == KeyEvent.VK_K) {
			if (Game.isNpc() == true) {
				Game.setNpc(false);
				Game.npcKill();
				System.out.println("[GAME] Dummy has been despawned");
			}
		}
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

}
