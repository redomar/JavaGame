package com.redomar.game.entities;

import com.redomar.game.InputHandler;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Player extends Mob {

	private InputHandler input;
	private int colour = Colours.get(-1, 111, 240, 310);

	public Player(LevelHandler level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
	}

	public void tick() {
		int xa = 0;
		int ya = 0;

		if (input.up.isPressed()) {
			ya--;
		}
		if (input.down.isPressed()) {
			ya++;
		}
		if (input.left.isPressed()) {
			xa--;
		}
		if (input.right.isPressed()) {
			xa++;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;

		int modifier = 8 * scale;
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		screen.render(xOffset, yOffset, xTile + yTile * 32, colour);
		screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32, colour);
		screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32, colour);
		screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour);
	}

	public boolean hasCollided(int xa, int ya) {
		return false;
	}

}
