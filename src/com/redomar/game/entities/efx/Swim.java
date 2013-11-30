package com.redomar.game.entities.efx;

import com.redomar.game.level.LevelHandler;

public class Swim {
	
	private static LevelHandler level;
	private int x;
	private int y;

	public Swim(LevelHandler level, int x, int y){
		Swim.level = level;
		this.x = x;
		this.y = y;
	}
	
	public boolean water(boolean isSwimming){
		if (level.getTile(x >> 3, y >> 3).getId() == 4) {
			isSwimming = true;
		}

		if (isSwimming
				&& level.getTile(x >> 3, y >> 3).getId() != 4) {
			isSwimming = false;
		}
		return isSwimming;
	}
	
	public boolean magma(boolean isMagma){
		if (level.getTile(x >> 3, y >> 3).getId() == 12) {
			isMagma = true;
		}

		if (isMagma
				&& level.getTile(x >> 3, y >> 3).getId() != 12) {
			isMagma = false;
		}
		
		return isMagma;
	}
	
	public boolean mud(boolean isMuddy){
		if (level.getTile(x >> 3, y >> 3).getId() == 14) {
			isMuddy = true;
		}

		if (isMuddy
				&& level.getTile(x >> 3, y >> 3).getId() != 14) {
			isMuddy = false;
		}
		
		return isMuddy;
	}
	
	public boolean[] swimming(boolean isSwimming, boolean isMagma, boolean isMuddy) {
		boolean[] swimminhType;
		swimminhType = new boolean[3];
		swimminhType [0] = water(isSwimming);
		swimminhType [1] = magma(isMagma);
		swimminhType [2] = mud(isMuddy);
		return swimminhType;
	}
}
