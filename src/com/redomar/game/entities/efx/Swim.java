package com.redomar.game.entities.efx;

import com.redomar.game.level.LevelHandler;

import java.util.HashMap;
import java.util.Map;

public class Swim {

	private static final Map<WaterType, int[]> COLORS = new HashMap<>();
	private static LevelHandler level;

	static {
		COLORS.put(WaterType.SWIMMING, new int[]{255, 255, 115});
		COLORS.put(WaterType.MAGMA, new int[]{541, 521, 510});
		COLORS.put(WaterType.SWAMP, new int[]{422, 410, 321});
	}

	private final int x;
	private final int y;

	public Swim(LevelHandler level, int x, int y) {
		Swim.level = level;
		this.x = x;
		this.y = y;
	}

	@Deprecated
	public int[] waveCols(boolean isSwimming, boolean isMagma, boolean isMuddy) {
		return COLORS.get(isSwimming ? WaterType.SWIMMING : isMagma ? WaterType.MAGMA : isMuddy ? WaterType.SWAMP : null);
	}

	public int[] waveCols(WaterType type) {
		return COLORS.get(type);
	}

	public boolean water(boolean isSwimming) {
		if (level.getTile(x >> 3, y >> 3).getId() == 4) {
			isSwimming = true;
		}

		if (isSwimming && level.getTile(x >> 3, y >> 3).getId() != 4) {
			isSwimming = false;
		}
		return isSwimming;
	}

	public boolean magma(boolean isMagma) {
		if (level.getTile(x >> 3, y >> 3).getId() == 12) {
			isMagma = true;
		}

		if (isMagma && level.getTile(x >> 3, y >> 3).getId() != 12) {
			isMagma = false;
		}

		return isMagma;
	}

	public boolean mud(boolean isMuddy) {
		if (level.getTile(x >> 3, y >> 3).getId() == 14) {
			isMuddy = true;
		}

		if (isMuddy && level.getTile(x >> 3, y >> 3).getId() != 14) {
			isMuddy = false;
		}

		return isMuddy;
	}

	public boolean[] swimming(boolean isSwimming, boolean isMagma, boolean isMuddy) {
		boolean[] swimmingType;
		swimmingType = new boolean[3];
		swimmingType[0] = water(isSwimming);
		swimmingType[1] = magma(isMagma);
		swimmingType[2] = mud(isMuddy);
		return swimmingType;
	}

	public boolean isActive(boolean[] swimmingType) {
		if (swimmingType[0]) {
			return true;
		} else if (swimmingType[1]) {
			return true;
		} else return swimmingType[2];
	}
}
