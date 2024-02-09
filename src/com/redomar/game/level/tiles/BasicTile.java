package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class BasicTile extends Tile {

	protected int tileId;
	protected final int tileColour;

	public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, false, false, levelColour);
		this.tileId = x + y * 32;
		this.tileColour = tileColour;
	}

	@Override
	public void tick() {
		// Basic tiles do not update
	}

	@Override
	public void render(Screen screen, LevelHandler level, int x, int y) {
		screen.render(x, y, tileId, tileColour, 0x00, 1);
	}

}
