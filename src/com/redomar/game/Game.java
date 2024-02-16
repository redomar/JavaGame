package com.redomar.game;

import com.redomar.game.audio.AudioHandler;
import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.Player;
import com.redomar.game.entities.Vendor;
import com.redomar.game.entities.trees.Spruce;
import com.redomar.game.event.InputHandler;
import com.redomar.game.event.MouseHandler;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.gfx.lighting.Night;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Either;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Time;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;
import org.apache.commons.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.im.InputContext;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;

/*
 * This module forms the core architecture of the JavaGame. It coordinates the various
 * audio and input handler components, generates the frame, renders the screen graphics, spawns
 * NPCs and customizes the player. Game is also responsible for changing the maps and levels, as well
 * as displaying various messages on the screen (e.g. fps)
 */
public class Game extends Canvas implements Runnable {

	// Setting the size and name of the frame/canvas
	@Serial
	private static final long serialVersionUID = 1L;
	private static final String game_Version = "v1.8.7 Alpha";
	private static final int SCALE = 100;
	private static final int WIDTH = 3 * SCALE;
	private static final int SCREEN_WIDTH = WIDTH * 2;
	private static final int HEIGHT = (2 * SCALE);
	private static final int SCREEN_HEIGHT = (HEIGHT * 2) + 30;
	private static final Screen screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
	private static final Screen screen2 = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
	private static final String NAME = "Game";                          // The name of the JFrame panel
	private static final Time time = new Time();                        // Represents the calendar's time value, in hh:mm:ss
	private static final boolean[] alternateCols = new boolean[2];      // Boolean array describing shirt and face colour
	private static Game game;
	// The properties of the player, npc, and fps/tps
	private static boolean changeLevel = false;                         // Determines whether the player teleports to another level
	private static boolean npc = false;                                 // Non-player character (NPC) initialized to non-existing
	private static int map = 0;                                         // Map of the level, initialized to map default map
	private static int shirtCol;                                        // The colour of the character's shirt
	private static int faceCol;                                         // The colour (ethnicity) of the character (their face)
	private static int fps;                                             // The frame rate (frames per second), frequency at which images are displayed on the canvas
	private static int tps;                                             // The ticks (ticks per second), unit measure of time for one iteration of the game logic loop.
	private static int steps;
	private static boolean devMode;                                     // Determines whether the game is in developer mode
	private static boolean closingMode;                                 // Determines whether the game will exit
	private static int tileX = 0;
	private static int tileY = 0;
	// Audio, input, and mouse handler objects
	private static JFrame frame;
	private static AudioHandler backgroundMusic;
	private static boolean running = false;                             // Determines whether the game is currently in process
	private static InputHandler input;                                  // Accepts keyboard input and follows the appropriate actions
	private static MouseHandler mouse;                                  // Tracks mouse movement and clicks, and follows the appropriate actions
	private static InputContext context;                                // Provides methods to control text input facilities
	// Graphics
	private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private final BufferedImage image3 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData(); // Array of red, green and blue values for each pixel
	private final int[] pixels3 = ((DataBufferInt) image3.getRaster().getDataBuffer()).getData(); // Array of red, green and blue values for each pixel
	private final int[] colours = new int[6 * 6 * 6];                   // Array of 216 unique colours (6 shades of red, 6 of green, and 6 of blue)
	private final BufferedImage image2 = new BufferedImage(WIDTH, HEIGHT - 30, BufferedImage.TYPE_INT_RGB);
	private final Font font = new Font();                               // Font object capable of displaying 2 fonts: Arial and Segoe UI
	private final Printer printer = new Printer();
	boolean musicPlaying = false;
	private int tickCount = 0;
	private LevelHandler level;                                         // Loads and renders levels along with tiles, entities, projectiles and more.
	//The entities of the game
	private Player player;
	private Dummy dummy;                                                // Dummy NPC follows the player around
	private Vendor vendor;                                              // Vendor NPC exhibits random movement and is only found on custom_level

	/**
	 * @author Redomar
	 */
	public Game() {
		context = InputContext.getInstance();

		// The game can only be played in one distinct window size
		setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setMaximumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		setFrame(new JFrame(NAME));                                     // Creates the frame with a defined name
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // Exits the program when user closes the frame
		getFrame().setLayout(new BorderLayout());
		getFrame().add(this, BorderLayout.CENTER);                // Centers the canvas inside the JFrame
		getFrame().pack();                                              // Sizes the frame so that all its contents are at or above their preferred sizes
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);                         // Centres the window on the screen
		getFrame().setVisible(true);

		requestFocus();
		setDevMode(false);
		setClosing(false);
	}

	/**
	 * This method will spawn a dummy NPC into the level only if they are allowed to be spawned in.
	 * They will be spawned at position (100, 150)  with a red shirt and caucasian face.
	 */
	public static void npcSpawn() {
		// If NPCs are allowed in the level
		if (isNpc()) {
			// Create a new dummy NPC to the current game level
			game.setDummy(new Dummy(game.level, "Dummy", 100, 150, 500, 543));
			game.level.addEntity(Game.getDummy());
		}
	}

	/**
	 * This method will remove a dummy NPC from the level only if they are not allowed to be in it.
	 */
	public static void npcKill() {
		if (!isNpc()) {        // If NPCs are not allowed in the level
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

	/**
	 * Sets the level to the map [.png] provided. Starts at x100 y100.
	 *
	 * @param Map_str Also sets predefined character colours.
	 */
	public void setMap(String Map_str) {
		setLevel(new LevelHandler(Map_str));
		if (alternateCols[0]) {                                         // If the first element (shirt colour) is set to True
			Game.setShirtCol(240);                                      // The player's shirt colour will be green
		}
		if (!alternateCols[0]) {                                        // If the first element (shirt colour) is set to False
			Game.setShirtCol(111);                                      // The player's shirt colour will be black
		}
		if (alternateCols[1]) {                                         // If the last element (face colour) is set to True
			Game.setFaceCol(310);                                       // The player will be African
		}
		if (!alternateCols[1]) {                                        // If the last element (face colour) is set to False
			Game.setFaceCol(543);                                       // The player will be caucasian
		}
		setPlayer(new Player(level, 100, 100, input, "", shirtCol, faceCol));
		level.addEntity(player);
		// Tree -- Spruce
		Spruce spruce = new Spruce(level, 70, 170, 2);
		level.addEntity(spruce);
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

	// Sets ethnicity/face colour for the player
	public static void setAlternateColsR(boolean alternateCols) {
		Game.alternateCols[1] = alternateCols;
	}

	// Sets the shirt colour for the player
	public static void setAlternateColsS(boolean alternateCols) {
		Game.alternateCols[0] = alternateCols;
	}

	public static AudioHandler getBackgroundMusic() {
		return Game.backgroundMusic;
	}

	public static void setBackgroundMusic(AudioHandler backgroundMusic) {
		Game.backgroundMusic = backgroundMusic;
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

	private static void mousePositionTracker() {
		MouseHandler mouseHandler = Game.getMouse();
		int mouseX = mouseHandler.getX();
		int mouseY = mouseHandler.getY();

		// Adjust mouse coordinates based on the current offset and scale of the game world
		tileX = ((mouseX + 4 + screen.getxOffset()) / (8 * 2)) + screen.getxOffset() / 16;
		tileY = ((mouseY + 4 + screen.getyOffset()) / (8 * 2)) + screen.getyOffset() / 16;
	}

	public static int getTileX() {
		return tileX;
	}

	public static int getTileY() {
		return tileY;
	}

	public static Screen getScreen() {
		return screen;
	}

	/*
	 * This method initializes the game once it starts. It populates the colour array with actual colours (6 shades each of RGB).
	 * This method also builds the initial game level (custom_level), spawns a new vendor NPC, and begins accepting keyboard and mouse input/tracking.
	 */
	public void init() {
		setGame(this);
		int index = 0;
		for (int r = 0; r < 6; r++) {                           // For all 6 shades of red
			for (int g = 0; g < 6; g++) {                   // For all 6 shades of green
				for (int b = 0; b < 6; b++) {           // For all 6 shades of blue
					int rr = (r * 255 / 5);         // Split all 256 colours into 6 shades (0, 51, 102 ... 255)
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					// All colour values (RGB) are placed into one 32-bit integer, populating the colour array
					// The first 8 bits are for alpha, the next 8 for red, the next 8 for green, and the last 8 for blue
					// 0xFF000000 is ignored in BufferedImage.TYPE_INT_RGB, but is used in BufferedImage.TYPE_INT_ARGB
					colours[index++] = 0xFF << 24 | rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen.setViewPortHeight(SCREEN_HEIGHT);
		screen2.setViewPortHeight(SCREEN_HEIGHT);
		screen.setViewPortWidth(SCREEN_WIDTH);
		screen2.setViewPortWidth(SCREEN_WIDTH);
		input = new InputHandler(this);                           // Input begins to record key presses
		setMouse(new MouseHandler(this));                         // Mouse tracking and clicking is now recorded
//		setWindow(new WindowHandler(this));
		try {
			setMap("/levels/custom_level.png");
		} catch (Exception e) {
			printer.print(e.toString(), PrintTypes.ERROR);
		}
		setMap(1);                                                      // 1 corresponds to custom_level

		game.setVendor(new Vendor(level, "Vendor", 215, 215, 304, 543));
		level.addEntity(getVendor());
	}

	/**
	 * This method will start the game and allow the user to start playing
	 */
	public synchronized void start() {
		Game.setRunning(true);                                          // Game will run
		new Thread(this, "GAME").start();                  // Thread is an instance of Runnable. Whenever it is started, it will run the run() method
	}

	/**
	 * This method will stop the game
	 */
	public synchronized void stop() {
		Game.getBackgroundMusic().close();
		Game.setRunning(false);                                         // Game will not run
	}

	/**
	 * This method forms the game loop, determining how the game runs. It runs throughout the entire game,
	 * continuously updating the game state and rendering the game.
	 */
	public void run() {
		long lastTime = System.nanoTime();
		int nsPerS = 1_000_000_000;
		double nsPerTick = nsPerS / 60D;                           // The number of nanoseconds in one tick (number of ticks limited to 60 per update)
		// 1 billion nanoseconds in one second
		int ticks = 0;
		int frames = 0;

		long lastTimer = System.nanoTime();                    // Used for updating ticks and frames once every second
		double delta = 0;

		init();                                                         // Initialize the game environment

		while (Game.isRunning()) {
			long now = System.nanoTime();                               // Current time (now) compared to lastTime to calculate elapsed time
			delta += (now - lastTime) / nsPerTick;                      // Elapsed time in seconds multiplied by 60
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {                                        // Once 1/60 seconds or more have passed
				ticks++;
				tick();                                                 // Tick updates
				delta -= 1;                                             // Delta becomes less than one again and the loop will close
				shouldRender = true;                                    // Limits the frames to 60 per second
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.nanoTime() - lastTimer >= nsPerS) {       // If elapsed time is greater than or equal to 1 second, update
				lastTimer += nsPerS;                                      // Updates in another second
				getFrame().setTitle("JavaGame - Version " + WordUtils.capitalize(game_Version).substring(1, game_Version.length()));
				fps = frames;
				tps = ticks;
				frames = 0;                                             // Reset the frames once per second
				ticks = 0;                                              // Reset the ticks once per second
			}
		}

	}

	/**
	 * This method updates the logic of the game.
	 */
	public void tick() {
		setTickCount(getTickCount() + 1);
		Either<Exception, Boolean> musicKeyAction = input.toggleActionWithCheckedRunnable(input.getM_KEY(), musicPlaying, () -> Game.getBackgroundMusic().play(), () -> Game.getBackgroundMusic().stop());
		musicKeyAction.either(exception -> {
			printer.cast().print("Failed to play music", PrintTypes.MUSIC);
			printer.exception(exception.toString());
			musicPlaying = false;
		}, isPlaying -> {
			musicPlaying = isPlaying;
			if (musicPlaying && !Game.getBackgroundMusic().getActive()) {
				input.overWriteKey(input.getM_KEY(), false);
			}
		});
		level.tick();
	}

	/**
	 * This method displays the current state of the game.
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}


		int xOffset = (int) getPlayer().getX() - (screen.getWidth() / 2);
		int yOffset = (int) getPlayer().getY() - (screen.getHeight() / 2);

		level.renderTiles(screen, xOffset, yOffset);
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
		for (int y = 0; y < screen2.getHeight(); y++) {
			for (int x = 0; x < screen2.getWidth(); x++) {
				int colourCode = screen2.getPixels()[x + y * screen2.getWidth()];
				if (colourCode < 1){
					pixels3[x + y * WIDTH] = 0xff0000;
				} else if (colourCode < 255) {
					pixels3[x + y * WIDTH] = colours[colourCode];
				}
			}
		}

		if (isChangeLevel() && getTickCount() % 60 == 0) {
			Game.setChangeLevel(true);
			setChangeLevel(false);
		}

		if (changeLevel) {
			printer.print("Teleported into new world", PrintTypes.GAME);
			if (getMap() == 1) {
				setMap("/levels/water_level.png");
				if (getDummy() != null) {

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

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight() - 30, null);
		status(g, isDevMode(), isClosing());
		overlayRender(g);
		g.drawImage(image2, 0, getHeight() - 30, getWidth(), getHeight(), null);
		g.setColor(Color.WHITE);
		g.setFont(font.getSegoe());
		g.drawString("Welcome " + WordUtils.capitalize(player.getName()), 3, getHeight() - 17);
		g.setColor(Color.ORANGE);

		if (context.getLocale().getCountry().equals("BE") || context.getLocale().getCountry().equals("FR")) {
			g.drawString("Press A to quit", (getWidth() / 2) - ("Press A to quit".length() * 3), getHeight() - 17);
		} else {
			g.drawString("Press Q to quit", (getWidth() / 2) - ("Press Q to quit".length() * 3), getHeight() - 17);
		}

		g.setColor(Color.YELLOW);
		g.drawString(time.getTime(), (getWidth() - 63), (getHeight() - 3));
		if (devMode) {
			g.setColor(Color.CYAN);
			g.drawString("Debug Mode Enabled", (getWidth() - 153), (getHeight() - 17));
		}
		g.setColor(Color.GREEN);

		if (backgroundMusic.getActive()) {
			g.drawString("MUSIC is ON ", 3, getHeight() - 3);
		}

		g.dispose();
		bs.show();
	}

	/**
	 * This method renders the overlay of the game, which is a transparent layer that is drawn over the game.
	 */
	private void overlayRender(Graphics2D g) {
		g.setColor(new Color(0f, 0f, 0f, 0f)); // Transparent color
		g.fillRect(0, 0, getWidth(), getHeight()-30);
	}

	/*
	 * This method displays information regarding various aspects/stats of the game, dependent upon
	 * whether it is running in developer mode, or if the application is closing.
	 */
	private void status(Graphics2D g, boolean TerminalMode, boolean TerminalQuit) {
		if (TerminalMode) {
			new Night(g, screen).render(player.getPlayerAbsX(), player.getPlayerAbsY());
			// make the background transparent
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, 195, 165);
			g.setColor(Color.CYAN);
			g.drawString("JavaGame Stats", 0, 10);
			g.drawString("FPS/TPS: " + fps + "/" + tps, 0, 25);
			if ((player.getNumSteps() & 15) == 15) {
				steps += 1;
			}
			g.drawString("Foot Steps: " + steps, 0, 40);
			g.drawString("NPC: " + WordUtils.capitalize(String.valueOf(isNpc())), 0, 55);
			g.drawString("Mouse: " + getMouse().getX() + "x |" + getMouse().getY() + "y", 0, 70);
			g.drawString("Mouse: " + (getMouse().getX() - 639 / 2d) + "x |" + (getMouse().getY() - 423 / 2d) + "y", 0, 85);
			if (getMouse().getButton() != -1) g.drawString("Button: " + getMouse().getButton(), 0, 100);
			mousePositionTracker();
			g.drawString("Player: " + (int) player.getX() + "x |" + (int) player.getY() + "y", 0, 115);
			double angle = Math.atan2(getMouse().getY() - player.getPlayerAbsY(), getMouse().getX() - player.getPlayerAbsX()) * (180.0 / Math.PI);
			g.drawString("Angle: " + angle, 0, 130);

			g.setColor(Color.cyan);
			g.drawString("Player: \t\t\t\t\t\t\t\t\t\t\t\t" + player.getPlayerAbsX() + "x |" + player.getPlayerAbsY() + "y", 0, 145);
			g.drawString("Player Offset: \t" + screen.getxOffset() + "x |" + screen.getyOffset() + "y", 0, 160);

			// Set a different color for the player-origin line
			g.setStroke(new BasicStroke(1));
			g.setColor(Color.GREEN); // Green for the new line from the player's origin
			g.drawLine(player.getPlayerAbsX(), player.getPlayerAbsY(), getMouse().getX(), getMouse().getY()); // Draw the line from the player's origin to the cursor
			g.setColor(Color.DARK_GRAY);
			g.drawLine(getWidth() / 2 + 8, getHeight() / 2 - 8, getMouse().getX(), getMouse().getY()); // Draw the line from the player's origin to the cursor
			g.drawLine(getWidth() / 2 + 8, 0, getWidth() / 2 + 8, getHeight() - 30);
			g.drawLine(0, getHeight() / 2 - 8, getWidth(), getHeight() / 2 - 8);
			g.setColor(Color.yellow);
			g.fillRect(player.getPlayerAbsX(), player.getPlayerAbsY(), 1, 1);


		}
		// If the game is shutting off
		if (!TerminalQuit) {
			return;
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.RED);
		g.drawString("Shutting down the Game", (getWidth() / 2) - 70, (getHeight() / 2) - 8);
		g.dispose();
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
