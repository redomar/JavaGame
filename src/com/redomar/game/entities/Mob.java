package com.redomar.game.entities;

import com.redomar.game.level.LevelHandler;

public abstract class Mob extends Entity {

	protected String name;
	protected int speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;

	public Mob(LevelHandler level, String name, int x, int y, int speed) {
		super(level);
		this.name = name;
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	public void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if (!hasCollided(xa, ya)) {
			if (ya < 0) {
				movingDir = 0;
			}
			if (ya > 0) {
				movingDir = 1;
			}
			if (xa < 0) {
				movingDir = 2;
			}
			if (xa > 0) {
				movingDir = 3;
			}
			x += xa * speed;
			y += ya * speed;
		}
	}

	public abstract boolean hasCollided(int xa, int ya);

	public String getName() {
		return name;
	}
}
