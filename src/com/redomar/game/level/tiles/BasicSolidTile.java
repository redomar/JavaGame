package com.redomar.game.level.tiles;

public class BasicSolidTile extends BasicTile {

	/**
	 * This is a solid tile, but does not emit any light. This tile does not have any animations.
	 *
	 * @param id Unique ID for the Tile
	 * @param x Specifies the x-coordinate from the SpriteSheet, measured in 8 pixels. Must be 0 - 31
	 * @param y Specifies the y-coordinate from the SpriteSheet, measured in 8 pixels. Must be 0 - 31
	 * @param tileColour Colours from the SpriteSheet.
	 * @param levelColour Colours to be displayed in the Game World.
	 */
	public BasicSolidTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, x, y, tileColour, levelColour);
		this.solid = true;
	}

}
