package com.redomar.game;

import com.redomar.game.audio.AudioHandler;
import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.Player;
import com.redomar.game.entities.Vendor;
import com.redomar.game.entities.trees.Spruce;
import com.redomar.game.gfx.Screen;
import com.redomar.game.gfx.SpriteSheet;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Time;
import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;
import org.apache.commons.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.im.InputContext;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/*
 * This module forms the core architecture of the JavaGame. It coordinates the various
 * audio and input handler components, generates the frame, renders the screen graphics, spawns 
 * NPCs and customizes the player. Game is also responsible for changing the maps and levels, as well
 * as displaying various messages on the screen (e.g. fps)
 */
public class Game extends Canvas implements Runnable {

	// Setting the size and name of the frame/canvas
	private static final long serialVersionUID = 1L;
	private static final String game_Version = "v1.8.5 Alpha";
	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Game";                         // The name of the JFrame panel
	private static Game game;
	private static Time time = new Time();                             // Represents the calender's time value, in hh:mm:ss

	// The properties of the player, npc, and fps/tps
	private static int Jdata_Host;                                     // The host of a multiplayer game (only available in earlier versions)
	private static String Jdata_UserName = "";
	private static String Jdata_IP = "127.0.0.1";
	private static boolean changeLevel = false;                        // Determines whether the player teleports to another level
	private static boolean npc = false;                                // Non-player character (NPC) initialized to non-existing
	private static int map = 0;                                        // Map of the level, initialized to map default map
	private static int shirtCol;                                       // The colour of the character's shirt
	private static int faceCol;                                        // The colour (ethnicity) of the character (their face)
	private static boolean[] alternateCols = new boolean[2];           // Boolean array describing shirt and face colour
	private static int fps;                                            // The frame rate (frames per second), frequency at which images are displayed on the canvas
	private static int tps;                                            // The ticks (ticks per second), unit measure of time for one iteration of the game logic loop.
	private static int steps;
	private static boolean devMode;                                    // Determines whether the game is in developer mode
	private static boolean closingMode;                                // Determines whether the game will exit

	// Audio, input, and mouse handler objects
	private static JFrame frame;
	private static AudioHandler backgroundMusic;
	private static boolean running = false;                            // Determines whether the game is currently in process
	private static InputHandler input;                                 // Accepts keyboard input and follows the appropriate actions
	private static MouseHandler mouse;                                 // Tracks mouse movement and clicks, and follows the appropriate actions
	private static InputContext context;                               // Provides methods to control text input facilities
	private int tickCount = 0;

	// Graphics
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
		BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())      // Array of red, green and blue values for each pixel
		.getData();
	private int[] colours = new int[6 * 6 * 6];                                     // Array of 216 unique colours (6 shades of red, 6 of green, and 6 of blue)
	private BufferedImage image2 = new BufferedImage(WIDTH, HEIGHT - 30,
		BufferedImage.TYPE_INT_RGB);
	private Screen screen;
	private WindowHandler window;
	private LevelHandler level;                                        // Loads and renders levels along with tiles, entities, projectiles and more.

	//The entities of the game
	private Player player;
	private Dummy dummy;                            // Dummy NPC follows the player around
	private Vendor vendor;                          // Vendor NPC exhibits random movement and is only found on cutom_level
	private Spruce spruce;							// Tree -- Spruce
	private Font font = new Font();                 // Font object capable of displaying 2 fonts: Arial and Segoe UI
	private String nowPlaying;
	private boolean notActive = true;
	private int trigger = 0;
	private Printing print = new Printing();

	/**
	 * @author Redomar
	 * @version Alpha 1.8.4
	 */
	public Game() {
		context = InputContext.getInstance();

		// The game can only be played in one distinct window size
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));                                             // Creates the frame with a defined name
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              // Exits the program when user closes the frame
		getFrame().setLayout(new BorderLayout());
		getFrame().add(this, BorderLayout.CENTER);                       // Centers the canvas inside the JFrame
		getFrame().pack();                                                      // Sizes the frame so that all its contents are at or above their preferred sizes
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);                                 // Centres the window on the screen
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
		if (isNpc() == true) {        // If NPCs are allowed in the level
			game.setDummy(new Dummy(game.level, "Dummy", 100, 150, 500,        // Create a new dummy NPC on the current game level, with name 'Dummy'
				543));                                                        // at position (100, 150), with a red shirt and caucasian ethnicity
			game.level.addEntity(Game.getDummy());
		}
	}

	/**
	 * This method will remove a dummy NPC from the level only if they are not allowed to be in it. 
	 */
	public static void npcKill() {
		if (isNpc() == false) {        // If NPCs are not allowed in the level
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

	/**
	 * Sets the level to the map [.png] provided. Starts at x100 y100.
	 * @param Map_str
	 *
	 * Also sets predefined character colours.
	 */
	public void setMap(String Map_str) {
		setLevel(new LevelHandler(Map_str));
		if (alternateCols[0]) {                        // If the first element (shirt colour) is set to True
			Game.setShirtCol(240);                // The player's shirt colour will be green
		}
		if (!alternateCols[0]) {                        // If the first element (shirt colour) is set to False
			Game.setShirtCol(111);                // The player's shirt colour will be black
		}
		if (alternateCols[1]) {                        // If the last element (face colour) is set to True
			Game.setFaceCol(310);                // The player will be African
		}
		if (!alternateCols[1]) {                        // If the last element (face colour) is set to False
			Game.setFaceCol(543);                // The player will be caucasian
		}
		setPlayer(new Player(level, 100, 100, input,
			getJdata_UserName(), shirtCol, faceCol));
		level.addEntity(player);
		spruce = new Spruce(level, 70,170, 2 );
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

	public static void setAlternateCols(boolean[] alternateCols) {        // Boolean array should have a size of only two elements
		Game.alternateCols = alternateCols;
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
					colours[index++] = rr << 16 | gg << 8 | bb;        // All colour values (RGB) are placed into one 32-bit integer, populating the colour array
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);                                         // Input begins to record key presses
		setMouse(new MouseHandler(this));                                       // Mouse tracking and clicking is now recorded
		setWindow(new WindowHandler(this));
		try{
			setMap("/levels/custom_level.png");
		} catch (Exception e){
			System.err.println(e);
		}
		setMap(1);                                                                     // 1 corresponds to custom_level

		game.setVendor(new Vendor(level, "Vendor", 215, 215, 304, 543));
		level.addEntity(getVendor());
	}

	/**
	* This method will start the game and allow the user to start playing
	*/
	public synchronized void start() {
		Game.setRunning(true);                // Game will run
		new Thread(this, "GAME").start();        // Thread is an instance of Runnable. Whenever it is started, it will run the run() method
	}

	/**
	* This method will stop the game
	*/
	public synchronized void stop() {
		Game.setRunning(false);                // Game will not run
	}

	/**
	* This method forms the game loop, determining how the game runs. It runs throughout the entire game, 
	* continuously updating the game state and rendering the game.  
	*/
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;                // The number of nanoseconds in one tick (number of ticks limited to 60 per update)
		// 1 billion nanoseconds in one second
		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();                // Used for updating ticks and frames once every second
		double delta = 0;

		init();                                                        // Initialize the game environment

		while (Game.isRunning()) {
			long now = System.nanoTime();                // Current time (now) compared to lastTime to calculate elapsed time
			delta += (now - lastTime) / nsPerTick;                // Elapsed time in seconds multiplied by 60
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {                                // Once 1/60 seconds or more have passed
				ticks++;
				tick();                                        // Tick updates
				delta -= 1;                                // Delta becomes less than one again and the loop will close
				shouldRender = true;                        // Limits the frames to 60 per second
			}

			try {
				Thread.sleep(2);                        // Delays the thread by 2 milliseconds - prevents the loop from using too much CPU
			} catch (InterruptedException e) {                // If the current thread is interrupted, the interrupted status is cleared
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {                     // If elapsed time is greater than or equal to 1 second, update
				lastTimer += 1000;                                                // Updates in another second
				getFrame().setTitle(
					"JavaGame - Version "
						+ WordUtils.capitalize(game_Version).substring(
						1, game_Version.length()));
				fps = frames;
				tps = ticks;
				frames = 0;                                                       // Reset the frames once per second
				ticks = 0;                                                        // Reset the ticks once per second
			}
		}

	}

	/**
	* This method updates the logic of the game.  
	*/
	public void tick() {
		setTickCount(getTickCount() + 1);
		level.tick();
	}

	/**
	* This method displays the current state of the game. 
	*/
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);                                        // Creates a new bs with triple buffering, which reduces tearing and cross-image pixelation
			return;
		}

		// Centres the player in the middle of the screen
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

		// Copies pixel data from the screen into the game
		for (int y = 0; y < screen.getHeight(); y++) {
			for (int x = 0; x < screen.getWidth(); x++) {
				int colourCode = screen.getPixels()[x + y * screen.getWidth()];
				if (colourCode < 255) {                                                     // If it is a valid colour code
					pixels[x + y * WIDTH] = colours[colourCode];                        // Sets the corresponding pixel from the screen to the game
				}
			}
		}

		if (isChangeLevel() == true && getTickCount() % 60 == 0) {
			Game.setChangeLevel(true);
			setChangeLevel(false);
		}

		if (changeLevel == true) {                                                      // If the player is teleporting to a different level
			print.print("Teleported into new world", PrintTypes.GAME);
			if (getMap() == 1) {                                                    // If the player is currently on custom_level
				setMap("/levels/water_level.png");
				if (getDummy() != null) { // Gave nullPointerException(); upon
					// entering new world.
					level.removeEntity(getDummy());
					setNpc(false);
				}
				level.removeEntity(getVendor());                        // When teleporting away from custom_level, remove vendor NPC (always found on custom_level)
				setMap(2);
			} else if (getMap() == 2) {                                     // If the player is currently on water_level
				setMap("/levels/custom_level.png");
				level.removeEntity(getDummy());
				setNpc(false);
				level.addEntity(getVendor());                           // Add a vendor NPC - they are always found on custom_level
				setMap(1);
			}
			changeLevel = false;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawRect(0, 0, getWidth(), getHeight());                       // Creates a rectangle the same size as the screen
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

		if (context.getLocale().getCountry().equals("BE")                                       // If the player resides in Belgium or France (i.e. uses AZERTY keyboard)
			|| context.getLocale().getCountry().equals("FR")) {                             // Displays "Press A to quit" in orange at the bottom-middle portion of the screen
			g.drawString("Press A to quit", (getWidth() / 2)
				- ("Press A to quit".length() * 3), getHeight() - 17);
		} else {                                                                                // If the player resides anywhere else (i.e. uses QWERTY keyboard)
			g.drawString("Press Q to quit", (getWidth() / 2)                        // Displays "Press Q to quit" in orange at the bottom-middle portion of the screen
				- ("Press Q to quit".length() * 3), getHeight() - 17);
		}
		g.setColor(Color.YELLOW);
		g.drawString(time.getTime(), (getWidth() - 58), (getHeight() - 3));                     // Displays the current time in yellow in the bottom right corner of the screen (hh:mm:ss)
		g.setColor(Color.GREEN);
		if (backgroundMusic.getActive()) {                                                      // If music is turned on
			g.drawString("MUSIC is ON ", 3, getHeight() - 3);                    // Displays "MUSIC IS ON" in green in the bottom left corner of the screen.
		}
		g.dispose();                                                                            // Frees up memory and resources for graphics
		bs.show();
	}

	/*
	* This method displays information regarding various aspects/stats of the game, dependent upon
	* whether it is running in developer mode, or if the application is closing.
	*/
	private void status(Graphics g, boolean TerminalMode, boolean TerminalQuit) {
		if (TerminalMode) {                                                             // If running in developer mode
			g.setColor(Color.CYAN);
			g.drawString("JavaGame Stats", 0, 10);                               // Display "JavaGame Stats" in cyan at the bottom left of the screen
			g.drawString("FPS/TPS: " + fps + "/" + tps, 0, 25);                  // Display the FPS and TPS in cyan directly above "JavaGame Stats"
			if ((player.getNumSteps() & 15) == 15) {
				steps += 1;
			}
			g.drawString("Foot Steps: " + steps, 0, 40);                         // Display the number of "Foot Steps" (in cyan, above the previous)
			g.drawString(
				"NPC: " + WordUtils.capitalize(String.valueOf(isNpc())), 0,     // Displays whether the NPC is on the level (in cyan, above the previous)
				55);
			g.drawString("Mouse: " + getMouse().getX() + "x |"                         // Displays the position of the cursor (in cyan, above the previous)
				+ getMouse().getY() + "y", 0, 70);
			if (getMouse().getButton() != -1)                                              // If a mouse button is pressed
				g.drawString("Button: " + getMouse().getButton(), 0, 85);   // Displays the mouse button that is pressed (in cyan, above the previous)
			g.setColor(Color.CYAN);
			g.fillRect(getMouse().getX() - 12, getMouse().getY() - 12, 24, 24);
		}
		// If the game is shutting off
		if (!TerminalQuit) {
			return;
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());                               // Make the screen fully black
		g.setColor(Color.RED);
		g.drawString("Shutting down the Game", (getWidth() / 2) - 70,           // Display "Shutting down the Game" in red in the middle of the screen
			(getHeight() / 2) - 8);
		g.dispose();                                                                    // Free up memory for graphics
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
