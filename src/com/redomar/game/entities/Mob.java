package com.redomar.game.entities;

import java.util.Random;

import com.redomar.game.level.LevelHandler;
import com.redomar.game.level.tiles.Tile;

public abstract class Mob extends Entity {

	protected String name;
	protected Random random = new Random();
	protected double speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	protected boolean isSwimming = false;
	protected boolean isMagma = false;
	protected boolean isMuddy = false;
	protected boolean changeLevels = false;
	protected int ticker;

	public Mob(LevelHandler level, String name, int x, int y, double speed) {
		super(level);
		this.name = name;
		this.setX(x);
		this.setY(y);
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
		
		//Moving Directions
		//0 = Facing UP
		//1 = Facing Down
		//2 = Facing Left
		//3 = Facing Right
		
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
		
		for (int x = 0; x < Math.abs(xa); x++) {
			if (!hasCollided(abs(xa), ya)) {
				this.x += abs(xa) * (int) speed;
			}
		}
		
		for (int y = 0; y < Math.abs(ya); y++) {
			if (!hasCollided(xa, abs(ya))) {
				this.y += abs(ya) * (int) speed;
			}
		}
	}

	public boolean hasCollided(int xa, int ya){
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
	
	public boolean hasCollidedAlt(int xa, int ya){
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - c % 2 * 8) / 8;
			double yt = ((y + ya) - c / 2 * 8) / 8;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix, iy).isSolid()) solid = true;
		}
		return solid;
	}
	
	private int abs(int i){
		if (i < 0) return -1;
		return 1;
	}

	protected boolean isSolid(int xa, int ya, int x, int y) {

		if (level == null) {
			return false;
		}

		Tile lastTile = level.getTile(((int) this.getX() + x) >> 3,
				((int) this.getY() + y) >> 3);
		Tile newtTile = level.getTile(((int) this.getX() + x + xa) >> 3, ((int) this.getY()
				+ y + ya) >> 3);

		if (!lastTile.equals(newtTile) && newtTile.isSolid()) {
			return true;
		}

		return false;
	}

	protected void followMovementAI(int x, int y, int px, int py, int xa,
			int ya, Mob mob) {
		ya = 0;
		xa = 0;
		if (px > x)
			xa++;
		if (px < x)
			xa--;
		if (py > y)
			ya++;
		if (py < y)
			ya--;
		moveMob(xa, ya, mob);
	}

	protected void moveMob(int xa, int ya, Mob mob) {
		if (xa != 0 || ya != 0) {
			mob.move(xa, ya);
			mob.isMoving = true;
		} else {
			mob.isMoving = false;
		}
	}

	public String getName() {
		return name;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}

}
