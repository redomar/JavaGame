package com.redomar.game.entities;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;

public abstract class Entity {

	protected double x, y;
	protected int xTile, yTile;
	protected String name;
	protected LevelHandler level;
	protected Printer entityPrinter = new Printer(PrintTypes.ENTITY);

	public Entity(LevelHandler level) {
		init(level);
	}

	public final void init(LevelHandler level) {
		this.level = level;
	}

	public abstract void tick();

	public abstract void render(Screen screen, int xTile, int yTile);

	public abstract void render(Screen screen);

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getXTile() {
		return xTile;
	}

	public void setXTile(int xTile) {
		this.xTile = xTile;
	}

	public int getYTile() {
		return yTile;
	}

	public void setYTile(int yTile) {
		this.yTile = yTile;
	}
}
