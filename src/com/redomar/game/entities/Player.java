package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.InputHandler;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Name;
import com.redomar.game.net.packets.Packet02Move;

public class Player extends Mob {

	private InputHandler input;
	private static Name customeName = new Name();

	private int colour = Colours.get(-1, 111, 240, 310);
	private int tickCount = 0;
	private String userName;

	public static String guestPlayerName = customeName.setName("Player ");

	public Player(LevelHandler level, int x, int y, InputHandler input,
			String userName) {
		super(level, "Player", x, y, 1);
		this.input = input;
		this.userName = userName;
	}

	public void tick() {
		int xa = 0;
		int ya = 0;

		if (input != null) {
			if (input.getUp().isPressed()) {
				ya--;
			}
			if (input.getDown().isPressed()) {
				ya++;
			}
			if (input.getLeft().isPressed()) {
				xa--;
			}
			if (input.getRight().isPressed()) {
				xa++;
			}
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;

			Packet02Move packet = new Packet02Move(this.getUsername(),
					this.getX(), this.getY(), this.numSteps, this.isMoving,
					this.movingDir);
			packet.writeData(Game.getGame().getSocketClient());

		} else {
			isMoving = false;
		}

		if (level.getTile(this.getX() >> 3, this.getY() >> 3).getId() == 4) {
			isSwimming = true;
		}

		if (isSwimming
				&& level.getTile(this.getX() >> 3, this.getY() >> 3).getId() != 4) {
			isSwimming = false;
		}

		if (level.getTile(this.getX() >> 3, this.getY() >> 3).getId() == 11) {
			changeLevels = true;
		}

		if (level.getTile(this.getX() >> 3, this.getY() >> 3).getId() == 12) {
			isMagma = true;
		}

		if (isMagma
				&& level.getTile(this.getX() >> 3, this.getY() >> 3).getId() != 12) {
			isMagma = false;
		}

		tickCount++;
	}

	public void render(Screen screen) {
		int xTile = 0;
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

		if (changeLevels) {
			Game.setChangeLevel(true);
		}

		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, 310);

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

			colour = Colours.get(-1, 111, -1, 310);

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

		screen.render((xOffset + (modifier * flipTop)), yOffset,
				(xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset,
				((xTile + 1) + yTile * 32), colour, flipTop, scale);
		if (!isSwimming && !isMagma) {
			screen.render((xOffset + (modifier * flipBottom)),
					(yOffset + modifier), (xTile + (yTile + 1) * 32), colour,
					flipBottom, scale);
			screen.render((xOffset + modifier - (modifier * flipBottom)),
					(yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32),
					colour, flipBottom, scale);
			colour = Colours.get(-1, 111, 240, 310);
			;
		}

		if (userName != null) {
			Font.render(userName, screen, xOffset
					- ((userName.length() - 1) / 2 * 8), yOffset - 10,
					Colours.get(-1, -1, -1, 555), 1);
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

	public String getUsername() {
		if (this.userName.isEmpty()) {
			return guestPlayerName;
		}
		return this.userName;
	}

	public void setUsername(String name) {
		this.userName = name;
	}

	public String getSantizedUsername() {
		if (this.getUsername() == null || this.userName.isEmpty()) {
			setUsername(guestPlayerName);
			return guestPlayerName;
		} else
			return this.getUsername();
	}

}
