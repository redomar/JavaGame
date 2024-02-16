package com.redomar.game.gfx;

public class Screen {

	private static final int MAP_WIDTH = 64;
	private static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	private static final byte BIT_MIRROR_X = 0x01;
	private static final byte BIT_MIRROR_Y = 0x02;
	private final SpriteSheet sheet;
	private int[] pixels;
	private int xOffset = 0;
	private int yOffset = 0;
	private int width;
	private int height;

	private int viewPortWidth;
	private int viewPortHeight;

	/**
	 * Constructs the draw area
	 *
	 * @param width  width of the screen
	 * @param height height of the screen
	 * @param sheet  Sprite-sheet selector. Constructed sprite sheet needs to be here,
	 *               Sprite-sheet cp requires path only.
	 */
	public Screen(int width, int height, SpriteSheet sheet) {

		this.setWidth(width);
		this.setHeight(height);
		this.sheet = sheet;

		setPixels(new int[width * height]);
	}

	@SuppressWarnings("unused")
	public static int getMapWidthMask() {
		return MAP_WIDTH_MASK;
	}


	/**
	 * Rendering sprites from sprite sheet onto the game world.
	 * Render constructor requires
	 *
	 * @param xPos      X Position of subject
	 * @param yPos      Y Position of subject
	 * @param tile      tile location. e.g 7 * 32 + 1 is the oblong bullet on the 7th row 2nd column
	 * @param colour    Using established colouring nomenclature. i.e. use com.redomar.game.gfx.Colours
	 * @param mirrorDir flip Direction: 0x01 flip vertical, 0x02 flip horizontal.
	 * @param scale     Scale
	 */
	public void render(int xPos, int yPos, int tile, int colour, int mirrorDir, int scale) {
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

				int col = (colour >> (sheet.pixels[xSheet + ySheet * sheet.getWidth() + tileOffset] * 8)) & 255;
				if (col < 255) {

					for (int yScale = 0; yScale < scale; yScale++) {

						if (yPixel + yScale < 0 | yPixel + yScale >= getHeight()) {
							continue;
						}

						for (int xScale = 0; xScale < scale; xScale++) {

							if (xPixel + xScale < 0 | xPixel + xScale >= getWidth()) {
								continue;
							}

							getPixels()[(xPixel + xScale) + (yPixel + yScale) * getWidth()] = col;
						}
					}

				}
			}
		}

	}
	private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";

	public void renderText(String msg, int xPos, int yPos, int colour, int scale) {
		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			if (charIndex >= 0) { // Only render if the character is found
				int tileInSprite = charIndex + 30 * 32; // Calculate the tile position based on the charIndex
				renderCharacter(xPos + i * (8 * scale), yPos, tileInSprite, colour, scale);
			}
		}
	}

	private void renderCharacter(int xPos, int yPos, int tile, int colour, int scale) {
		xPos -= xOffset;
		yPos -= yOffset;

		int xTile = tile % 32;
		int yTile = tile / 32;
		int tileOffset = (xTile << 3) + (yTile << 3) * sheet.getWidth();

		for (int y = 0; y < 8; y++) {
			int yPixel = yPos + y * scale;

			for (int x = 0; x < 8; x++) {
				int xPixel = xPos + x * scale;

				int col = (colour >> (sheet.pixels[x + y * sheet.getWidth() + tileOffset] * 8)) & 255;
				if (col < 255) {
					for (int yScale = 0; yScale < scale; yScale++) {
						if (yPixel + yScale < 0 || yPixel + yScale >= height) continue;
						for (int xScale = 0; xScale < scale; xScale++) {
							if (xPixel + xScale < 0 || xPixel + xScale >= width) continue;
							pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
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

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
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

	public int getViewPortWidth() {
		return viewPortWidth;
	}

	public void setViewPortWidth(int viewPortWidth) {
		this.viewPortWidth = viewPortWidth;
	}

	public int getViewPortHeight() {
		return viewPortHeight;
	}

	public void setViewPortHeight(int viewPortHeight) {
		this.viewPortHeight = viewPortHeight;
	}
}
