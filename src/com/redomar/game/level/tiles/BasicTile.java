package com.redomar.game.level.tiles;

import com.redomar.game.Game;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class BasicTile extends Tile {

	protected final int tileColour;
	protected int tileId;

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
		int[] colours = Colours.getColours(tileColour);
		int mouseOverTileX = Game.getTileX();
		int mouseOverTileY = Game.getTileY();

		if (mouseOverTileX == x / 8 && mouseOverTileY == y / 8) {
			screen.render(x, y, tileId, Colours.highlight(colours[0], colours[1], colours[2], colours[3]), 0x00, 1);
		} else {
			screen.render(x, y, tileId, Colours.get(colours[0], colours[1], colours[2], colours[3]), 0x00, 1);
		}
	}

}
