package com.redomar.game.entities;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public abstract class Entity {

	protected int x, y;
	protected LevelHandler level;

	public Entity(LevelHandler level) {
		init(level);
	}

	public final void init(LevelHandler level) {
		this.level = level;
	}

	public abstract void tick();

	public abstract void render(Screen screen);

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
