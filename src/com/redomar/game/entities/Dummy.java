package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Dummy extends Mob {

	private int colour, shirtCol, faceCol; // = Colours.get(-1, 111, 240, 310);
	private int tickCount = 0;
	private int xa = 0;
	private int ya = 0;
	
	private Swim swim; 

	public Dummy(LevelHandler level, String name, int x, int y, int shirtCol,
			int faceCol) {
		super(level, "h", x, y, 1);
		this.faceCol = faceCol;
		this.shirtCol = shirtCol;
		this.colour = Colours.get(-1, 111, shirtCol, faceCol);
	}

	public void tick() {

		followMovementAI(getX(), getY(), Game.getPlayer().getX(), Game
				.getPlayer().getY(), xa, ya, this);
		
		swimming();

		tickCount++;

	}
	
	private void swimming(){
		setSwim(new Swim(level, getX(), getY()));
		
		isSwimming = getSwim().water(isSwimming);
		isMagma = getSwim().magma(isMagma);
		isMuddy = getSwim().mud(isMuddy);
	}

	public void render(Screen screen) {
		int xTile = 8;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (movingDir == 1) {
			xTile += 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}

		int modifier = 8 * scale;
		int xOffset = getX() - modifier / 2;
		int yOffset = getY() - modifier / 2 - 4;

		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, faceCol);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, 255, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, 115, -1, 225);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, 225, 115);
			}

			screen.render(xOffset, yOffset + 3, 31 + 31 * 32, waterColour,
					0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 31 + 31 * 32, waterColour,
					0x01, 1);
		}

		if (isMagma) {
			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, faceCol);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, 541, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, 521, 510, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, 510, -1, 521);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, 521, 510);
			}

			screen.render(xOffset, yOffset + 3, 31 + 31 * 32, waterColour,
					0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 31 + 31 * 32, waterColour,
					0x01, 1);
		}
		
		if (isMuddy) {
			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, 310);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, 422, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, 410, 321, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, 321, -1, 410);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, 410, 321);
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
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isSolid(xa, ya, x, yMin)) {
				return true;
			}
		}

		for (int x = xMin; x < xMax; x++) {
			if (isSolid(xa, ya, x, yMax)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolid(xa, ya, xMin, y)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolid(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	public Swim getSwim() {
		return swim;
	}

	public void setSwim(Swim swim) {
		this.swim = swim;
	}
}
