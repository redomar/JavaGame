package com.redomar.game.entities.projectiles;

import java.util.Random;

import com.redomar.game.entities.Entity;
import com.redomar.game.level.LevelHandler;

public abstract class Projectile extends Entity{

	protected final double xOrigin, yOrigin;
	protected double angle;
	protected double nx, ny;
	protected double speed, range, damage, distance;
	protected Random life = new Random();
	
	private boolean removed = false;
	
	public Projectile(LevelHandler level, int x, int y, double dir) {
		super(level);
		xOrigin = x;
		yOrigin = y;
		angle = dir;
		this.x = x;
		this.y = y;
	}

	protected abstract void move();
	
	public void remove(){
		setRemoved(true);
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
}
