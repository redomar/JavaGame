package com.redomar.game.entities.trees;

import com.redomar.game.entities.Entity;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public abstract class Tree extends Entity {

	/**
	 * Tree abstract class
	 * -- Used by Spruce Class
	 */

	//Position variables
	protected final double x;
	protected final double y;

	/**
	 * Constructor for tree entities
	 *
	 * @param level LevelHandler in which tree spawns
	 * @param x     X co-ordinate
	 * @param y     Y co-ordinate
	 */
	Tree(LevelHandler level, double x, double y) {
		super(level);
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(Screen screen, int xTile, int yTile) {

	}
}
