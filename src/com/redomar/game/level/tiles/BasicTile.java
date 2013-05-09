package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class BasicTile extends Tile {

	protected int tileId;
	protected int tileColour;

	public BasicTile(int id, int x, int y, int tileColour) {
		super(id, false, false);
		
		this.tileId = x + y;
		this.tileColour = tileColour;
	}

	public void render(Screen screen, LevelHandler level, int x, int y) {
		screen.render(x, y, tileId, tileColour, 0x00);
	}

}
