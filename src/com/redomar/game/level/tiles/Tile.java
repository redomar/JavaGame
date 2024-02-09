package com.redomar.game.level.tiles;

import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;


@SuppressWarnings({"StaticInitializerReferencesSubClass", "OctalInteger"})
public abstract class Tile {

	private static final Tile[] tiles = new Tile[256];

	static {
		/* VOID */
		tiles[0] = new SolidTile(0, 0, 0, Colours.get(0, -1, -1, -1), 0xFF000000);
		/* STONE */
		tiles[1] = new SolidTile(1, 1, 0, Colours.get(-1, 444, 333, -1), 0xFF555555);
		/* CHISELED_stone */
		tiles[2] = new BasicTile(2, 2, 0, Colours.get(-1, 333, 222, -1), 0xFF666666);
		/* GRASS */
		tiles[3] = new BasicTile(3, 3, 0, Colours.get(-1, 131, 141, -1), 0xFF00FF00);
		/* WATER */
		tiles[4] = new AnimatedTile(4, new int[][]{{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colours.get(-1, 004, 115, -1), 0xFF0000FF, 1000);
		/* FLOWER_rose */
		tiles[5] = new BasicTile(5, 4, 0, Colours.get(131, 151, 510, 553), 0xFFCCFF33);
		/* FLOWER_dandelion */
		tiles[6] = new BasicTile(6, 4, 0, Colours.get(131, 151, 553, 510), 0xFFFFCC33);
		/* SAND */
		tiles[7] = new BasicTile(7, 5, 0, Colours.get(-1, 553, 554, 555), 0xFFFFFF99);
		/* CHEST_a */
		tiles[8] = new SolidTile(8, 0, 1, Colours.get(333, 111, 420, 000), 0xFFFF0001);
		/* CHEST_b */
		tiles[9] = new SolidTile(9, 1, 1, Colours.get(333, 111, 420, 000), 0xFFFF0002);
		/* CARPET_red */
		tiles[10] = new BasicTile(10, 5, 0, Colours.get(-1, 311, 411, 311), 0xFFAA3636);
		/* PORTAL */
		tiles[11] = new AnimatedTile(11, new int[][]{{3, 5}, {4, 5}, {5, 5}, {6, 5}, {7, 5}, {8, 5}, {9, 5}, {10, 5}}, Colours.get(-1, 005, 305, -1), 0xFF00EAFF, 100);
		/* MAGMA */
		tiles[12] = new AnimatedTile(12, new int[][]{{0, 5}, {1, 5}, {2, 5}, {1, 5}}, Colours.get(-1, 400, 511, -1), 0xFFF00F0F, 1000);
		/* DIRT */
		tiles[13] = new BasicTile(13, 3, 0, Colours.get(0, 210, 321, -1), 0xFF442200);
		/* DIRT_WET */
		tiles[14] = new AnimatedTile(14, new int[][]{{1, 5}, {2, 5}}, Colours.get(-1, 211, 322, -1), 0xFF663300, 1500);
	}

	protected final byte id;
	protected final boolean emitter;
	protected final int levelColour;
	protected boolean solid;

	protected Tile(int id, boolean solid, boolean emitter, int levelColour) {
		this.id = (byte) id;
		this.solid = solid;
		this.emitter = emitter;
		this.levelColour = levelColour;


		tiles[id] = this;
	}

	public static Tile getTile(int id) {
		return tiles[id];
	}

	public static Tile getStone() {
		return getTile(1);
	}

	public static Tile getGrass() {
		return getTile(2);
	}

	public static Tile getVoid() {
		return getTile(0);
	}

	public static Tile[] getTiles() {
		return tiles;
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
