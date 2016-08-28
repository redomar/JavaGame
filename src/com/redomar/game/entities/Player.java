package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.InputHandler;
import com.redomar.game.audio.AudioEffect;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.entities.projectiles.Medium;
import com.redomar.game.entities.projectiles.Projectile;
import com.redomar.game.entities.projectiles.Small;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Name;
import com.redomar.game.objects.Inventory;

public class Player extends Mob {

	private static Name customeName = new Name();
	public static String guestPlayerName = customeName.setName("Player ");
	private static double speed = 1;
	private static int[] collisionBoders = {-2, 8, 0, 7};
	private InputHandler input;
	private Swim swim;
	private int colour, shirtCol, faceCol;
	private int tickCount = 0;
	private String userName;
	private boolean[] swimType;
	private int[] swimColour;
	private int fireRate = 0;

	public Player(LevelHandler level, int x, int y, InputHandler input,
				  String userName, int shirtCol, int faceCol) {
		super(level, "Player", x, y, speed, collisionBoders);
		this.input = input;
		this.userName = userName;
		this.faceCol = faceCol;
		this.shirtCol = shirtCol;
		this.colour = Colours.get(-1, 111, shirtCol, faceCol);
		fireRate = Small.FIRE_RATE;
	}

	public static double getSpeed() {
		return speed;
	}

	public static void setSpeed(double speed) {
		Player.speed = speed;
	}

	public void tick() {
		double xa = 0;
		double ya = 0;

		if (input != null) {
			if (input.getUp().isPressed() && input.isIgnoreInput() == false) {
				ya -= speed;
			}
			if (input.getDown().isPressed() && input.isIgnoreInput() == false) {
				ya += speed;
			}
			if (input.getLeft().isPressed() && input.isIgnoreInput() == false) {
				xa -= speed;
			}
			if (input.getRight().isPressed() && input.isIgnoreInput() == false) {
				xa += speed;
			}
		}

		if(fireRate > 0) fireRate--;

		if (Game.getMouse().getButton() == 1 || Game.getMouse().getButton() == 3){
			if(fireRate <= 0){
				if(Game.getMouse().getButton()== 1){
					fireRate = Small.FIRE_RATE;
				}else if(Game.getMouse().getButton() == 3){
					fireRate = Medium.FIRE_RATE;
				}
				if(!swim.isActive(swimType)){
					double dx = Game.getMouse().getX() - 480/2;
					double dy = Game.getMouse().getY() - 320/2;
					double dir = Math.atan2(dy, dx);
					shoot(x, y, dir, Game.getMouse().getButton(), false);
				}
			}
		}

		for (int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			if(p.isRemoved()){
				projectiles.remove(i);
				Game.getLevel().removeProjectileEntities(p);
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
			Game.getGame();

		} else {
			isMoving = false;
		}

		setSwim(new Swim(level, (int) getX(), (int) getY()));
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
			if (!isMoving || swim.isActive(swimType)){
				yTile -= 2;
			}
		} else if (movingDir == 0 && !isMoving || movingDir == 0 && swim.isActive(swimType)) {
			yTile -= 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			if(!isMoving){
				xTile = 4;
			}
		}

		int modifier = 8 * scale;
		int xOffset = (int) getX() - modifier / 2;
		int yOffset = (int) getY() - modifier / 2 - 4;

		if (changeLevels) {
			Game.setChangeLevel(true);
		}

		if(isSwimming || isMagma || isMuddy){
			swimColour = swim.waveCols(isSwimming, isMagma, isMuddy);

			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, faceCol);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, swimColour[0], -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, swimColour[1], swimColour[2], -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, swimColour[2], -1, swimColour[1]);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, swimColour[1], swimColour[2]);
			}

			screen.render(xOffset, yOffset + 3, 31 + 31 * 32, waterColour,
					0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 31 + 31 * 32, waterColour,
					0x01, 1);
		}

		screen.render((xOffset + (modifier * flipTop)), yOffset,
				(xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset,
				((xTile + 1) + yTile * 32), colour, flipTop, scale);
		if (!isSwimming && !isMagma && !isMuddy) {
			screen.render((xOffset + (modifier * flipBottom)),
					(yOffset + modifier), (xTile + (yTile + 1) * 32), colour,
					flipBottom, scale);
			screen.render((xOffset + modifier - (modifier * flipBottom)),
					(yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32),
					colour, flipBottom, scale);
			colour = Colours.get(-1, 111, shirtCol, faceCol);
		}

		if (userName != null) {
			/*
			 * Improved userName centering above player's sprite.
			 * Using player's own x value cast to int with an adjusted formula
			 * -posmicanomaly
			 */
			int fontCharSize = 8;
			Font.render(userName,
					screen,
					(int)x - ((userName.length() /2) * fontCharSize),
					yOffset - 10,
					Colours.get(-1, -1, -1, 555), 1);

		}
	}

	public String getUsername() {
		if (this.userName.isEmpty()) {
			return guestPlayerName;
		}
		return this.userName;
	}

	public void setUsername(String name) {
		this.userName = name;
	}

	public String getSanitisedUsername() {
		if (this.getUsername() == null || this.userName.isEmpty()) {
			setUsername(guestPlayerName);
			return guestPlayerName;
		} else
			return this.getUsername();
	}

	public Swim getSwim() {
		return swim;
	}

	public void setSwim(Swim swim) {
		this.swim = swim;
	}

}
