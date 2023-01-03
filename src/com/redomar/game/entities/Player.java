package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.InputHandler;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.entities.projectiles.Medium;
import com.redomar.game.entities.projectiles.Projectile;
import com.redomar.game.entities.projectiles.Small;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.HashGen;
import com.redomar.game.objects.Inventory;

import java.util.Objects;

public class Player extends Mob {

	private static final String PLAYER_ID = new HashGen(false, 3).getHash();
	private static final int[] COLLISION_BORDERS = {-2, 8, 0, 7};
	private static final double speed = 1;
	private final InputHandler inputHandler;
	private final int shirtColour;
	private final int faceColour;
	private int colour;
	private int tickCount = 0;
	private Swim swim;
	private boolean[] swimType;
	private int fireRate;

	public Player(LevelHandler level, int x, int y, InputHandler inputHandler, String name, int shirtColour, int faceColour) {
		super(level, "Player", x, y, speed, COLLISION_BORDERS);
		this.inputHandler = inputHandler;
		this.name = !Objects.equals(name, "") ? name : String.format("Player %s", PLAYER_ID);
		this.faceColour = faceColour;
		this.shirtColour = shirtColour;
		this.colour = Colours.get(-1, 111, shirtColour, faceColour);
		fireRate = Small.FIRE_RATE;
	}

	public void tick() {
		double xa = 0;
		double ya = 0;

		if (inputHandler != null) {
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
					double dx = Game.getMouse().getX() - 480 / 2d;
					double dy = Game.getMouse().getY() - 320 / 2d;
					double dir = Math.atan2(dy, dx);
					shoot(x, y, dir, Game.getMouse().getButton(), false);
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

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		Inventory.activate();

		if (movingDir == 1) {
			xTile += 2;
			if (!isMoving || swim.isActive(swimType)) {
				yTile -= 2;
			}
		} else if (movingDir == 0 && !isMoving || movingDir == 0 && swim.isActive(swimType)) {
			yTile -= 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			if (!isMoving) {
				xTile = 4;
			}
		}

		int modifier = 8 * scale;
		int xOffset = (int) getX() - modifier / 2;
		int yOffset = (int) getY() - modifier / 2 - 4;

		if (changeLevels) {
			Game.setChangeLevel(true);
		}

		if (isSwimming || isMagma || isMuddy) {
			int[] swimColour = swim.waveCols(isSwimming, isMagma, isMuddy);

			int waterColour;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, faceColour);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, swimColour[0], -1);
			} else if (tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, swimColour[1], swimColour[2], -1);
			} else if (tickCount % 60 < 45) {
				waterColour = Colours.get(-1, swimColour[2], -1, swimColour[1]);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, swimColour[1], swimColour[2]);
			}

			screen.render(xOffset, yOffset + 3, 31 + 31 * 32, waterColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 31 + 31 * 32, waterColour, 0x01, 1);
		}

		screen.render((xOffset + (modifier * flipTop)), yOffset, (xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset, ((xTile + 1) + yTile * 32), colour, flipTop, scale);
		if (!isSwimming && !isMagma && !isMuddy) {
			screen.render((xOffset + (modifier * flipBottom)), (yOffset + modifier), (xTile + (yTile + 1) * 32), colour, flipBottom, scale);
			screen.render((xOffset + modifier - (modifier * flipBottom)), (yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32), colour, flipBottom, scale);
			colour = Colours.get(-1, 111, shirtColour, faceColour);
		}

		if (name != null) {
			/*
			 * Improved name centering above player's sprite.
			 * Using player's own x value cast to int with an adjusted formula
			 * -posmicanomaly
			 */

			int fontCharSize = 8;
			int offsetUnit = ((name.length() & 1) == 0 ? fontCharSize / 2 : 0);
			int nameOffset = (name.length() / 2) * fontCharSize - offsetUnit;
			Font.render(name, screen, (int) x - nameOffset, yOffset - 10, Colours.get(-1, -1, -1, 111), 1);

		}
	}

	@Deprecated
	public String getUsername() {
		return this.name;
	}

	@Deprecated
	public String getSanitisedUsername() {
		return this.name;
	}

}
