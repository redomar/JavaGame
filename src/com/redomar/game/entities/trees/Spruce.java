package com.redomar.game.entities.trees;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public class Spruce extends Tree {

	/**
	 * Spruce Tree by MOHAMED OMAR
	 *
	 */

	//BIT MASK variables in Bits. Used to select bits by using comparing.
	private final int BIT_MASK_MOVE_RIGHT = 0b0001;
	private final int BIT_MASK_MOVE_UP_1 = 0b0010;
	private final int BIT_MASK_MOVE_UP_2 = 0b0100;

	private int scale;

	/**
	 * Spruce tree
	 * @param level LevelHandler level which spruces spawns
	 * @param x X co-ordinate
	 * @param y Y co-ordinate
	 * @param scale Size of tree
	 */
	public Spruce(LevelHandler level, double x, double y, int scale) {
		super(level, x, y);
		this.setX((int)x);
		this.setY((int)y);
		this.setName("Spruce_Tree");
		this.setScale(scale);
	}

	@Override
	public void tick() {

	}

	/**
	 * This Renders the spruce tree
	 *
	 * Spruce is 3x2 on the sprite sheet
	 *
	 *      0    1
	 *    +---------+
	 * 6  |0100|0101|
	 * 7  |0010|0011|
	 * 8  |0000|0001|
	 *    +---------+
	 *
	 * @param screen Screen to render on.
	 */
	public void render(Screen screen) {

		//Spruce uses 6 tiles from sprite sheet.
		int spruceSize = 0;

		//This will go through a while loop for each 8*8 section of the spruce
		while (spruceSize < 6){
			int right = 0;
			int up1 = 0;
			int up2 = 0;
			int tileX = 0;
			int tileY = 4;


			//BIT MASK for all the right sided sprites. comparing it to 0b0001 (last bit).
			if ((spruceSize & BIT_MASK_MOVE_RIGHT) > 0){
				//MOVE RIGHT
				right = scale * 8;
				tileX = tileX + 1;
			}

			//it will only move up 1 time if it does not move up twice.
			if ((spruceSize & BIT_MASK_MOVE_UP_2) > 0){
				//MOVE UP 2 TIMES
				up2 = scale * 8;
				tileY = tileY -2;
			} else if ((spruceSize & BIT_MASK_MOVE_UP_1) > 0){
				//MOVE UP ONCE
				up1 = scale * 8;
				tileY = tileY -1;
			}

			//rendering to game. Inline if statement ensures that tree trunk has a unique colour
			screen.render((int)getX()+right,((int)getY() - up1) - up2*2,(tileY*32+tileX), Colours.get(-1,020,241, spruceSize > 0b0001? 251: 110), 0x00, scale);

			spruceSize++;
		}

	}

	//Some getters and setters
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
