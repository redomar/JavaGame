package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.level.LevelHandler;

public class Dummy extends Mob {

	private static final double SPEED = 0.75;
	private static final int[] MOB_TILE = {8, 28};
	private static final int[] COLLISION_BORDERS = {0, 7, 0, 7};

	public Dummy(LevelHandler level, String name, int x, int y, int shirtCol, int faceCol) {
		super(level, name, x, y, MOB_TILE, SPEED, COLLISION_BORDERS, shirtCol, faceCol);
	}

	public void tick() {
		aStarMovementAI((int) getX(), (int) getY(), (int) Game.getPlayer().getX(), (int) Game.getPlayer().getY(), SPEED, this);


		setSwim(new Swim(level, (int) getX(), (int) getY()));
		swimType = swim.swimming(isSwimming, isMagma, isMuddy);
		isSwimming = swimType[0];
		isMagma = swimType[1];
		isMuddy = swimType[2];

		tickCount++;

	}

	public void setSwim(Swim swim) {
		this.swim = swim;
	}
}
