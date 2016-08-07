package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class BasicTile extends Tile {

	protected int tileId;
	protected int tileColour;

	/**
	 * This is a tile that does not emit light, nor is sold. This tile does not have any animations.
	 *
	 * @param id Unique ID for the Tile
	 * @param x Specifies the x-coordinate from the SpriteSheet, measured in 8 pixels. Must be 0 - 31
	 * @param y Specifies the y-coordinate from the SpriteSheet, measured in 8 pixels. Must be 0 - 31
	 * @param tileColour Colours from the SpriteSheet.
	 * @param levelColour Colours to be displayed in the Game World.
	 */
	public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, false, false, levelColour);

		this.tileId = x + y * 32;
		this.tileColour = tileColour;
	}

	public void tick() {
	}

	public void render(Screen screen, LevelHandler level, int x, int y) {
		screen.render(x, y, tileId, tileColour, 0x00, 1);
	}

}
