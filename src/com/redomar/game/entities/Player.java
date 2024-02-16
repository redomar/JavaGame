package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.entities.projectiles.Medium;
import com.redomar.game.entities.projectiles.Projectile;
import com.redomar.game.entities.projectiles.Small;
import com.redomar.game.event.InputHandler;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.HashGen;

import java.util.Objects;

public class Player extends Mob {

	private static final String PLAYER_ID = new HashGen(false, 3).getHash();
	private static final int[] PLAYER_TILE = {0, 28};
	private static final int[] COLLISION_BORDERS = {-2, 8, 0, 7};
	private static double speed = 1;
	private final InputHandler inputHandler;
	private int fireRate;
	private int playerAbsX;
	private int playerAbsY;

	public Player(LevelHandler level, int x, int y, InputHandler inputHandler, String name, int shirtColour, int faceColour) {
		super(level, "Player", x, y, PLAYER_TILE, speed, COLLISION_BORDERS, shirtColour, faceColour);
		this.inputHandler = inputHandler;
		this.name = !Objects.equals(name, "") ? name : String.format("Player %s", PLAYER_ID);
		fireRate = Small.FIRE_RATE;
		showName = true;
	}

	public void tick() {
		double xa = 0;
		double ya = 0;

		// Calculate and set player's absolute X and Y positions
		setPlayerAbsX((((int) getX() - Game.getScreen().getxOffset()) * 2) + 8);
		setPlayerAbsY((((int) getY() - Game.getScreen().getyOffset()) * 2) + 7);


		if (inputHandler != null) {

			speed = inputHandler.getSHIFTED().isPressed() ? 2.5D : 1D;

			if (inputHandler.getUP_KEY().isPressed()) {
				ya -= speed;
			}
			if (inputHandler.getDOWN_KEY().isPressed()) {
				ya += speed;
			}
			if (inputHandler.getLEFT_KEY().isPressed()) {
				xa -= speed;
			}
			if (inputHandler.getRIGHT_KEY().isPressed()) {
				xa += speed;
			}
		}

		if (fireRate > 0) fireRate--;

		if (Game.getMouse().getButton() == 1 || Game.getMouse().getButton() == 3) {
			if (fireRate <= 0) {
				if (Game.getMouse().getButton() == 1) {
					fireRate = Small.FIRE_RATE;
				} else if (Game.getMouse().getButton() == 3) {
					fireRate = Medium.FIRE_RATE;
				}
				if (!swim.isActive(swimType)) {

					// Cursor position
					int cursorX = Game.getMouse().getX();
					int cursorY = Game.getMouse().getY();

					// Calculate differences (dx, dy) between cursor and origin
					double dx = cursorX - playerAbsX;
					double dy = cursorY - playerAbsY;

					// Calculate direction using atan2
					double dir = Math.atan2(dy, dx);

					// Continue with shooting logic
					shoot(x, y, dir, Game.getMouse().getButton());

					entityPrinter.highlight().print("Direction: " + dir + "º\t" + dx + "x\t" + dy + "y");
				}
			}
		}

		for (Projectile p : projectiles) {
			if (p.isRemoved()) {
				p.remove();
				Game.getLevel().removeProjectileEntities(p);
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

		swim = new Swim(level, (int) getX(), (int) getY());
		swimType = swim.swimming(isSwimming, isMagma, isMuddy);
		isSwimming = swimType[0];
		isMagma = swimType[1];
		isMuddy = swimType[2];

		if (level.getTile((int) this.getX() >> 3, (int) this.getY() >> 3).getId() == 11) {
			changeLevels = true;
		}

		tickCount++;
	}

	@Deprecated
	public String getUsername() {
		return this.name;
	}

	@Deprecated
	public String getSanitisedUsername() {
		return this.name;
	}

	public int getPlayerAbsX() {
		return playerAbsX;
	}

	public void setPlayerAbsX(int playerAbsX) {
		this.playerAbsX = playerAbsX;
	}

	public int getPlayerAbsY() {
		return playerAbsY;
	}

	public void setPlayerAbsY(int playerAbsY) {
		this.playerAbsY = playerAbsY;
	}
}
