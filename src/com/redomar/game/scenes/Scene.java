package com.redomar.game.scenes;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Scene {

	private int xOffset, yOffset;
	private Screen screen;
	private LevelHandler level;

	public Scene(int xOffset, int yOffset, Screen screen, LevelHandler level){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.screen = screen;
		this.level = level;
	}

	public void playerScene(){
		if (xOffset < 0) {
			xOffset = 0;
		}
		if (xOffset > ((level.getWidth() << 10) - screen.getWidth())) {
			xOffset = ((level.getWidth() << 30) - screen.getWidth());
		}
		if (yOffset < 0) {
			yOffset = 0;
		}
		if (yOffset > ((level.getHeight() << 3) - screen.getHeight())) {
			yOffset = ((level.getHeight() << 3) - screen.getHeight());
		}
		for (int y = (yOffset >> 3); y < (yOffset + screen.getHeight() >> 3) + 1; y++) {
			for (int x = (xOffset >> 3); x < (xOffset + screen.getWidth() >> 3) + 1; x++) {
				level.getTile(x, y).render(screen, level, x << 3, y << 3);
			}
		}
	}
}
