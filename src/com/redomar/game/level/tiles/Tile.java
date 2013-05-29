package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(0,
			-1, -1, -1), 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(
			-1, 444, 333, -1), 0xFF555555);
	public static final Tile CHISELED_STONE = new BasicTile(2, 2, 0,
			Colours.get(-1, 333, 222, -1), 0xFF666666);
	public static final Tile GRASS = new BasicTile(3, 3, 0, Colours.get(-1,
			131, 141, -1), 0xFF00FF00);
	public static final Tile WATER = new AnimatedTile(4, new int[][] {
			{ 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } }, Colours.get(-1, 004, 115,
			-1), 0xFF0000FF, 1000);
	public static final Tile FLOWER_rose = new BasicTile(5, 4, 0, Colours.get(
			131, 151, 510, 553), 0xFFCCFF33);
	public static final Tile FLOWER_dandelion = new BasicTile(6, 4, 0,
			Colours.get(131, 151, 553, 510), 0xFFFFCC33);
	public static final Tile SAND = new BasicTile(7, 5, 0, Colours.get(-1, 553,
			554, 555), 0xFFFFFF99);

	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int levelColour;

	public Tile(int id, boolean isSolid, boolean isEmitter, int colour) {
		this.id = (byte) id;

		if (tiles[id] != null) {
			throw new RuntimeException("Duplicate tile id on:" + id);
		}

		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = colour;

		tiles[id] = this;
	}

	public byte getId() {
		return id;
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isEmitter() {
		return emitter;
	}

	public abstract void tick();

	public abstract void render(Screen screen, LevelHandler level, int x, int y);

	public int getLevelColour() {
		return levelColour;
	}

}
