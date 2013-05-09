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

		screen.render((xOffset + (modifier * flipTop)), yOffset, (xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset, ((xTile + 1) + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + (modifier * flipBottom)), (yOffset + modifier), (xTile	+ (yTile + 1) * 32), colour, flipBottom, scale);
		screen.render((xOffset + modifier - (modifier * flipBottom)), (yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32), colour, flipBottom, scale);
	}

	public boolean hasCollided(int xa, int ya) {
		return false;
	}

}
