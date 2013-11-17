package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;

public abstract class Tile {

	private static final Tile[] tiles = new Tile[256];
	private static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(0,	-1, -1, -1), 0xFF000000);
	private static final Tile STONE = new BasicSolidTile(1, 1, 0, Colours.get(-1, 444, 333, -1), 0xFF555555);
	private static final Tile CHISELED_stone = new BasicTile(2, 2, 0, Colours.get(-1, 333, 222, -1), 0xFF666666);
	private static final Tile GRASS = new BasicTile(3, 3, 0, Colours.get(-1, 131, 141, -1), 0xFF00FF00);
	private static final Tile WATER = new AnimatedTile(4, new int[][] {	{ 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } }, Colours.get(-1, 004, 115,	-1), 0xFF0000FF, 1000);
	private static final Tile FLOWER_rose = new BasicTile(5, 4, 0, Colours.get(131, 151, 510, 553), 0xFFCCFF33);
	private static final Tile FLOWER_dandelion = new BasicTile(6, 4, 0,	Colours.get(131, 151, 553, 510), 0xFFFFCC33);
	private static final Tile SAND = new BasicTile(7, 5, 0, Colours.get(-1, 553, 554, 555), 0xFFFFFF99);
	private static final Tile CHEST_a = new BasicSolidTile(8, 0, 1, Colours.get(333, 111, 420, 000), 0xFFFF0001);
	private static final Tile CHEST_b = new BasicSolidTile(9, 1, 1, Colours.get(333, 111, 420, 000), 0xFFFF0002);
	private static final Tile CARPET_red = new BasicTile(10, 5, 0, Colours.get(-1, 311, 411, 311), 0xFFAA3636);
	private static final Tile PORTAL = new AnimatedTile(11, new int[][] { { 3,  5 },  { 4, 5 }, { 5, 5 }, { 6, 5 }, { 7, 5 }, { 8, 5 }, { 9, 5 }, { 10, 5 } },  Colours.get(-1, 005, 305, -1), 0xFF00EAFF, 100);
	private static final Tile MAGMA = new AnimatedTile(12, new int [][] { { 0, 5 }, { 1, 5 }, { 2, 5 }, { 1, 5 } }, Colours.get(-1, 400, 511, -1), 0xFFF00F0F, 1000);
	private static final Tile DIRT = new BasicTile(13, 3, 0, Colours.get(0, 210, 321, -1), 0xFF442200);
	private static final Tile DIRT_WET = new AnimatedTile(14, new int[][] { { 1, 5 }, { 2, 5 } }, Colours.get(-1, 211, 322, -1), 0xFF663300, 1500);

	protected byte id;
	protected boolean solid;
	protected boolean emitter;
	private int levelColour;

	public Tile(int id, boolean isSolid, boolean isEmitter, int colour) {
		this.id = (byte) id;

		if (getTiles()[id] != null) {
			throw new RuntimeException("Duplicate tile id on:" + id);
		}

		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = colour;

		getTiles()[id] = this;
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

	public static Tile getStone() {
		return STONE;
	}

	public static Tile getChiseledStone() {
		return CHISELED_stone;
	}

	public static Tile getGrass() {
		return GRASS;
	}

	public static Tile getFlowerRose() {
		return FLOWER_rose;
	}

	public static Tile getFlowerDandelion() {
		return FLOWER_dandelion;
	}

	public static Tile getSand() {
		return SAND;
	}

	public static Tile getWater() {
		return WATER;
	}

	public static Tile getVoid() {
		return VOID;
	}

	public static Tile[] getTiles() {
		return tiles;
	}

	public static Tile getChestA() {
		return CHEST_a;
	}

	public static Tile getChestB() {
		return CHEST_b;
	}

	public static Tile getCarpetRed() {
		return CARPET_red;
	}

	public static Tile getPortal() {
		return PORTAL;
	}

	public static Tile getMagma() {
		return MAGMA;
	}

	public static Tile getDirt() {
		return DIRT;
	}

	public static Tile getDirtWet() {
		return DIRT_WET;
	}

}
