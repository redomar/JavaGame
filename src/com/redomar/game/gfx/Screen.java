package com.redomar.game.gfx;

public class Screen {

	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	public int[] pixels;

	public int xOffset = 0;
	public int yOffset = 0;

	public int width;
	public int height;

	public SpriteSheet sheet;

	public Screen(int width, int height, SpriteSheet sheet) {

		this.width = width;
		this.height = height;
		this.sheet = sheet;

		pixels = new int[width * height];
	}

	public void render(int xPos, int yPos, int tile, int colour,
			boolean mirrorX, boolean mirrorY) {
		xPos -= xOffset;
		yPos -= yOffset;

		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.width;

		for (int y = 0; y < 8; y++) {
			int ySheet = y;

			if (mirrorY) {
				ySheet = 7 - y;
			}

			if (y + yPos < 0 | y + yPos >= height) {
				continue;
			}

			for (int x = 0; x < 8; x++) {
				int xSheet = x;

				if (mirrorX) {
					xSheet = 7 - x;
				}

				if (x + xPos < 0 | x + xPos >= width) {
					continue;
				}
				int col = (colour >> (sheet.pixels[xSheet + ySheet
						* sheet.width + tileOffset] * 8)) & 255;
				if (col < 255) {
					pixels[(x + xPos) + (y + yPos) * width] = col;
				}
			}
		}

	}
}
