package com.redomar.game.gfx;

public class Screen {

	private static final int MAP_WIDTH = 64;
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	private static final byte BIT_MIRROR_X = 0x01;
	private static final byte BIT_MIRROR_Y = 0x02;

	private int[] pixels;

	private int xOffset = 0;
	private int yOffset = 0;

	private int width;
	private int height;

	private SpriteSheet sheet;

	public Screen(int width, int height, SpriteSheet sheet) {

		this.setWidth(width);
		this.setHeight(height);
		this.sheet = sheet;

		setPixels(new int[width * height]);
	}

	public static int getMapWidthMask() {
		return MAP_WIDTH_MASK;
	}

	public void render(int xPos, int yPos, int tile, int colour, int mirrorDir,
					   int scale) {
		xPos -= xOffset;
		yPos -= yOffset;

		boolean mirrorX = (mirrorDir & BIT_MIRROR_X) > 0;
		boolean mirrorY = (mirrorDir & BIT_MIRROR_Y) > 0;

		int scaleMap = scale - 1;
		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.getWidth();

		for (int y = 0; y < 8; y++) {
			int ySheet = y;

			if (mirrorY) {
				ySheet = 7 - y;
			}

			int yPixel = y + yPos + (y * scaleMap) - ((scaleMap << 3) / 2);

			for (int x = 0; x < 8; x++) {
				int xSheet = x;

				if (mirrorX) {
					xSheet = 7 - x;
				}

				int xPixel = x + xPos + (x * scaleMap) - ((scaleMap << 3) / 2);

				int col = (colour >> (sheet.pixels[xSheet + ySheet
						* sheet.getWidth() + tileOffset] * 8)) & 255;
				if (col < 255) {

					for (int yScale = 0; yScale < scale; yScale++) {

						if (yPixel + yScale < 0
								| yPixel + yScale >= getHeight()) {
							continue;
						}

						for (int xScale = 0; xScale < scale; xScale++) {

							if (xPixel + xScale < 0
									| xPixel + xScale >= getWidth()) {
								continue;
							}

							getPixels()[(xPixel + xScale) + (yPixel + yScale)
									* getWidth()] = col;
						}
					}

				}
			}
		}

	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int[] getPixels() {
		return pixels;
	}

	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
