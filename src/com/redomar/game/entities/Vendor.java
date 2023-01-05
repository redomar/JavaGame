package com.redomar.game.entities;

import com.redomar.game.entities.efx.Swim;
import com.redomar.game.level.LevelHandler;

public class Vendor extends Mob {

	private static final double SPEED = 0.75;
	private static final int[] MOB_TILE = {0, 28};
	private static final int[] COLLISION_BORDERS = {0, 7, 0, 7};
	private int tick = 0;
	private double xa = 0;
	private double ya = 0;

	public Vendor(LevelHandler level, String name, int x, int y, int shirtCol, int faceCol) {
		super(level, name, x, y, MOB_TILE, SPEED, COLLISION_BORDERS, shirtCol, faceCol);
	}

	public void tick() {
		tick++;
		double[] movement = randomMovementAI(x, y, xa, ya, tick);

		this.xa = movement[0];
		this.ya = movement[1];

		moveMob(xa, ya, this);

		setSwim(new Swim(level, (int) getX(), (int) getY()));
		swimType = swim.swimming(isSwimming, isMagma, isMuddy);
		isSwimming = swimType[0];
		isMagma = swimType[1];
		isMuddy = swimType[2];

		tickCount++;
	}

}
