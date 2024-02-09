package com.redomar.game.gfx.lighting;

import com.redomar.game.Game;
import com.redomar.game.gfx.Screen;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Night {

	private final Graphics2D g;
	private final Screen screen;

	public Night(Graphics2D g, Screen screen) {
		this.g = g;
		this.screen = screen;
	}

	public void render(int playerX, int playerY) {
		double size = 160;

		g.setColor(new Color(0f, 0f, 0f, 0.8f));
		Shape rectangle = new Rectangle(0, 0, screen.getViewPortWidth(), screen.getViewPortHeight() - 30);
		Shape circle = new Ellipse2D.Double(playerX - size / 2, playerY - size / 2, size, size);
		int mouseX = Game.getMouse().getX();
		int mouseY = Game.getMouse().getY();

		// Adjust mouse coordinates based on the current offset and scale of the game world
		int tileX = ((mouseX) / (8 * 2));
		int tileY = ((mouseY) / (8 * 2));
		Shape smallRectangle = new Rectangle(tileX * 16 - 16, tileY * 16 - 16, 48, 48);
		Area area = new Area(rectangle);
		area.subtract(new Area(circle));
		area.subtract(new Area(smallRectangle));
		g.fill(area);
	}
}
