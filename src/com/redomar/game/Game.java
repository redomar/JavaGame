package com.redomar.game;

import com.redomar.game.audio.AudioHandler;
import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.Player;
import com.redomar.game.entities.Vendor;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Time;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;
import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.im.InputContext;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

	// Setting the size and name of the frame/canvas
	private static final long serialVersionUID = 1L;
	private static final String game_Version = "v1.8.3 Alpha";
	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Game";
	private static Game game;
	private static Time time = new Time();
	private static int Jdata_Host;
	private static String Jdata_UserName = "";
	private static String Jdata_IP = "127.0.0.1";
	private static boolean changeLevel = false;
	private static boolean npc = false;
	private static int map = 0;
	private static int shirtCol;
	private static int faceCol;
	private static boolean[] alternateCols = new boolean[2];
	private static int fps;
	private static int tps;
	private static int steps;
	private static boolean devMode;
	private static boolean closingMode;

	private static JFrame frame;
	private static AudioHandler backgroundMusic;
	private static boolean running = false;
	private static InputHandler input;
	private static MouseHandler mouse;
	private static InputContext context;
	private int tickCount = 0;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colours = new int[6 * 6 * 6];
	private BufferedImage image2 = new BufferedImage(WIDTH, HEIGHT - 30,
			BufferedImage.TYPE_INT_RGB);
	private Screen screen;
	private WindowHandler window;
	private LevelHandler level;
	private Player player;
	private Dummy dummy;
	private Vendor vendor;
	private Font font = new Font();
	private String nowPlaying;
	private boolean notActive = true;
	private int trigger = 0;
	private Printing print = new Printing();

	/**
	 * @author Redomar
	 * @version Alpha 1.8.3
	 */
	public Game() {
		context = InputContext.getInstance();
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().setLayout(new BorderLayout());
		getFrame().add(this, BorderLayout.CENTER);
		getFrame().pack();
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);
		getFrame().setVisible(true);

		requestFocus();
		setDevMode(false);
		setClosing(false);
	}

	public static void npcSpawn() {
		if (isNpc() == true) {
			game.setDummy(new Dummy(game.level, "Dummy", 100, 150, 500,
					543));
			game.level.addEntity(Game.getDummy());
		}
	}

	public static void npcKill() {
		if (isNpc() == false) {
			game.level.removeEntity(Game.getDummy());
		}
	}

	public static JFrame getFrame() {
		return Game.frame;
	}

	public static void setFrame(JFrame frame) {
		Game.frame = frame;
	}

	public static Player getPlayer() {
		return game.player;
	}

	public void setPlayer(Player player) {
		game.player = player;
	}

	public static LevelHandler getLevel() {
		return game.level;
	}

	public void setLevel(LevelHandler level) {
		this.level = level;
	}

	public static Time getTime() {
		return Game.time;
	}

	public void setTime(Time time) {
		Game.time = time;
	}

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Game.game = game;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Game.running = running;
	}

	public static boolean isChangeLevel() {
		return changeLevel;
	}

	public static void setChangeLevel(boolean changeLevel) {
		Game.changeLevel = changeLevel;
	}

	public static int getMap() {
		return map;
	}

	public void setMap(String Map_str) {
		setLevel(new LevelHandler(Map_str));
		if (alternateCols[0]) {
			Game.setShirtCol(240);
		}
		if (!alternateCols[0]) {
			Game.setShirtCol(111);
		}
		if (alternateCols[1]) {
			Game.setFaceCol(310);
		}
		if (!alternateCols[1]) {
			Game.setFaceCol(543);
		}
		setPlayer(new Player(level, 100, 100, input,
				getJdata_UserName(), shirtCol, faceCol));
		level.addEntity(player);
	}

	public static void setMap(int map) {
		Game.map = map;
	}

	public static boolean isNpc() {
		return npc;
	}

	public static void setNpc(boolean npc) {
		Game.npc = npc;
	}

	public static Dummy getDummy() {
		return game.dummy;
	}

	public void setDummy(Dummy dummy) {
		this.dummy = dummy;
	}

	public static String getJdata_IP() {
		return Jdata_IP;
	}

	public static void setJdata_IP(String jdata_IP) {
		Jdata_IP = jdata_IP;
	}

	public static int getJdata_Host() {
		return Jdata_Host;
	}

	public static void setJdata_Host(int jdata_Host) {
		Jdata_Host = jdata_Host;
	}

	public static String getJdata_UserName() {
		return Jdata_UserName;
	}

	public static void setJdata_UserName(String jdata_UserName) {
		Jdata_UserName = jdata_UserName;
	}

	public static String getGameVersion() {
		return game_Version;
	}

	public static int getShirtCol() {
		return shirtCol;
	}

	public static void setShirtCol(int shirtCol) {
		Game.shirtCol = shirtCol;
	}

	public static int getFaceCol() {
		return faceCol;
	}

	public static void setFaceCol(int faceCol) {
		Game.faceCol = faceCol;
	}

	public static boolean[] getAlternateCols() {
		return alternateCols;
	}

	public static void setAlternateCols(boolean[] alternateCols) {
		Game.alternateCols = alternateCols;
	}

	public static void setAlternateColsR(boolean alternateCols) {
		Game.alternateCols[1] = alternateCols;
	}

	public static void setAlternateColsS(boolean alternateCols) {
		Game.alternateCols[0] = alternateCols;
	}

	public static void setBackgroundMusic(AudioHandler backgroundMusic) {
		Game.backgroundMusic = backgroundMusic;
	}

	public static AudioHandler getBackgroundMusic(){
		return Game.backgroundMusic;
	}

	public static InputHandler getInput() {
		return input;
	}

	public void setInput(InputHandler input) {
		Game.input = input;
	}

	public static MouseHandler getMouse() {
		return mouse;
	}

	public static void setMouse(MouseHandler mouse) {
		Game.mouse = mouse;
	}

	public static boolean isDevMode() {
		return devMode;
	}

	public static void setDevMode(boolean devMode) {
		Game.devMode = devMode;
	}

	public static boolean isClosing() {
		return closingMode;
	}

	public static void setClosing(boolean closing) {
		Game.closingMode = closing;
	}

	public void init() {
		setGame(this);
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		setMouse(new MouseHandler(this));
		setWindow(new WindowHandler(this));
		setMap("/levels/custom_level.png");
		setMap(1);

		game.setVendor(new Vendor(level, "Vendor", 215, 215, 304, 543));
		level.addEntity(getVendor());
	}

	public synchronized void start() {
		Game.setRunning(true);
		new Thread(this, "GAME").start();
	}

	public synchronized void stop() {
		Game.setRunning(false);
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		init();

		while (Game.isRunning()) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				getFrame().setTitle(
						"JavaGame - Version "
								+ WordUtils.capitalize(game_Version).substring(
								1, game_Version.length()));
				fps = frames;
				tps = ticks;
				frames = 0;
				ticks = 0;
			}
		}

	}

	public void tick() {
		setTickCount(getTickCount() + 1);
		level.tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = (int) getPlayer().getX() - (screen.getWidth() / 2);
		int yOffset = (int) getPlayer().getY() - (screen.getHeight() / 2);

		level.renderTiles(screen, xOffset, yOffset);

		/*
		 * for (int x = 0; x < level.width; x++) { int colour = Colours.get(-1,
		 * -1, -1, 000); if (x % 10 == 0 && x != 0) { colour = Colours.get(-1,
		 * -1, -1, 500); } Font.render((x % 10) + "", screen, 0 + (x * 8), 0,
		 * colour, 1); }
		 */

		level.renderEntities(screen);
		level.renderProjectileEntities(screen);

		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				int colourCode = screen.getPixels()[x + y * screen.getWidth()];
				if (colourCode < 255) {
					pixels[x + y * WIDTH] = colours[colourCode];
				}
			}
		}

		if (isChangeLevel() == true && getTickCount() % 60 == 0) {
			Game.setChangeLevel(true);
			setChangeLevel(false);
		}

		if (changeLevel == true) {
			print.print("Teleported into new world", PrintTypes.GAME);
			if (getMap() == 1) {
				setMap("/levels/water_level.png");
				if (getDummy() != null) { // Gave nullPointerException(); upon
					// entering new world.
					level.removeEntity(getDummy());
					setNpc(false);
				}
				level.removeEntity(getVendor());
				setMap(2);
			} else if (getMap() == 2) {
				setMap("/levels/custom_level.png");
				level.removeEntity(getDummy());
				setNpc(false);
				level.addEntity(getVendor());
				setMap(1);
			}
			changeLevel = false;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight() - 30, null);
		status(g, isDevMode(), isClosing());
		// Font.render("Hi", screen, 0, 0, Colours.get(-1, -1, -1, 555), 1);
		g.drawImage(image2, 0, getHeight() - 30, getWidth(), getHeight(), null);
		g.setColor(Color.WHITE);
		g.setFont(font.getSegoe());
		g.drawString(
				"Welcome "
						+ WordUtils.capitalizeFully(player
						.getSanitisedUsername()), 3, getHeight() - 17);
		g.setColor(Color.ORANGE);

		if (context.getLocale().getCountry().equals("BE")
				|| context.getLocale().getCountry().equals("FR")) {
			g.drawString("Press A to quit", (getWidth() / 2)
					- ("Press A to quit".length() * 3), getHeight() - 17);
		} else {
			g.drawString("Press Q to quit", (getWidth() / 2)
					- ("Press Q to quit".length() * 3), getHeight() - 17);
		}
		g.setColor(Color.YELLOW);
		g.drawString(time.getTime(), (getWidth() - 58), (getHeight() - 3));
		g.setColor(Color.GREEN);
		if(backgroundMusic.getActive()) {
			g.drawString("MUSIC is ON ", 3, getHeight() - 3);
		}
		g.dispose();
		bs.show();
	}

	private void status(Graphics g, boolean TerminalMode, boolean TerminalQuit) {
		if (TerminalMode == true) {
			g.setColor(Color.CYAN);
			g.drawString("JavaGame Stats", 0, 10);
			g.drawString("FPS/TPS: " + fps + "/" + tps, 0, 25);
			if ((player.getNumSteps() & 15) == 15) {
				steps += 1;
			}
			g.drawString("Foot Steps: " + steps, 0, 40);
			g.drawString(
					"NPC: " + WordUtils.capitalize(String.valueOf(isNpc())), 0,
					55);
			g.drawString("Mouse: " + getMouse().getX() + "x |"
					+ getMouse().getY() + "y", 0, 70);
			if (getMouse().getButton() != -1)
				g.drawString("Button: " + getMouse().getButton(), 0, 85);
			g.setColor(Color.CYAN);
			g.fillRect(getMouse().getX() - 12, getMouse().getY() - 12, 24, 24);
		}
		if (TerminalQuit == true) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.RED);
			g.drawString("Shutting down the Game", (getWidth() / 2) - 70,
					(getHeight() / 2) - 8);
			g.dispose();
		}
	}

	public WindowHandler getWindow() {
		return window;
	}

	public void setWindow(WindowHandler window) {
		this.window = window;
	}

	public String getNowPlaying() {
		return nowPlaying;
	}

	public void setNowPlaying(String nowPlaying) {
		this.nowPlaying = nowPlaying;
	}

	public int getTickCount() {
		return tickCount;
	}

	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

}
