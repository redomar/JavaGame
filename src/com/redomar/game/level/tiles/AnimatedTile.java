package com.redomar.game.level.tiles;

public class AnimatedTile extends BasicTile {

	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;

	/**
	 *
	 * @param id Unique ID for the Tile
	 * @param animationCoords A 2D array of all x,y-coordinates in Integers.
	 * @param tileColour Colours from the SpriteSheet.
	 * @param levelColour Colours to be displayed in the Game World.
	 * @param animationSwitchDelay Length of time to be delayed to the next frame from the 2D array.
	 */
	public AnimatedTile(int id, int[][] animationCoords, int tileColour,
						int levelColour, int animationSwitchDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour,
				levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= (animationSwitchDelay)) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1)
					% animationTileCoords.length;
			this.tileId = (animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32));
		}
	}
}
