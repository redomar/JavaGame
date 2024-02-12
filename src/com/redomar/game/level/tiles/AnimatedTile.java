package com.redomar.game.level.tiles;

public class AnimatedTile extends BasicTile {

	private final int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private final int animationSwitchDelay;

	public AnimatedTile(int id, int[][] animationCoords, int tileColour, int levelColour, int animationSwitchDelay) {
		super(id, animationCoords[0][0], animationCoords[0][1], tileColour, levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	@Override
	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= animationSwitchDelay) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1) % animationTileCoords.length;
			this.tileId = animationTileCoords[currentAnimationIndex][0] + (animationTileCoords[currentAnimationIndex][1] * 32);
		}
	}
}
