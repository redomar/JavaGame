package com.redomar.game.entities;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Dummy extends Mob{
	
	private int colour = Colours.get(-1, 111, 240, 310);
	private int tickCount = 0;
	private int tick = 0;
	private int xa = 0;
	private int ya = 0;
	
	public Dummy(LevelHandler level, String name, int x, int y, int speed) {
		super(level, "h", x, y, 1);
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
	
	public void tick() {
		tick++;
		if(tick % (random.nextInt(50) +30) == 0){
			xa = random.nextInt(3) -1;
			ya = random.nextInt(3) -1;
			if(random.nextInt(4) == 0){
				xa = 0;
				ya = 0;
			}
		}
		
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
			
//			Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
//			packet.writeData(Game.getGame().getSocketClient());
			
		} else {
			isMoving = false;
		}

		if (level.getTile(this.x >> 3, this.y >> 3).getId() == 4) {
			isSwimming = true;
		}

		if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 4) {
			isSwimming = false;
		}

		tickCount++;
		
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
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, 543);

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

		screen.render((xOffset + (modifier * flipTop)), yOffset,
				(xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset,
				((xTile + 1) + yTile * 32), colour, flipTop, scale);
		if (!isSwimming) {
			screen.render((xOffset + (modifier * flipBottom)),
					(yOffset + modifier), (xTile + (yTile + 1) * 32), colour,
					flipBottom, scale);
			screen.render((xOffset + modifier - (modifier * flipBottom)),
					(yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32),
					colour, flipBottom, scale);
			colour = Colours.get(-1, 111, 054, 543);
		}
	}

}
