package com.redomar.game.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.lang3.text.WordUtils;

import com.redomar.game.Game;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Mouse;
import com.thehowtotutorial.splashscreen.JSplash;

public class Menu implements Runnable {

	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Menu";

	private static boolean running = false;
	private static boolean selectedStart = false;
	private static boolean selectedExit = false;
	private static boolean gameOver = false;

	private static DedicatedJFrame frame;// = new DedicatedJFrame(WIDTH, HEIGHT,
											// SCALE, NAME);
	private Font font = new Font();
	private MouseListener Mouse = new Mouse();
	private KeyListener Key = new MenuInput();

	private Color selected = new Color(0xFFFF8800);
	private Color deSelected = new Color(0xFFCC5500);

	public synchronized void start() {
		running = true;
		play();
		new Thread(this, "MENU").start();
	}

	public static synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 30D;

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;

			while (delta >= 1) {
				ticks++;
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
				frame.getFrame().setTitle(
						"Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void render() {
		// frame.getFrame().getContentPane().setBackground(Color.GREEN);
		frame.addMouseMotionListener((MouseMotionListener) Mouse);
		frame.addMouseListener(Mouse);
		frame.addKeyListener(Key);
		BufferStrategy bs = frame.getBufferStrategy();
		if (bs == null) {
			frame.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(new Color(0xFF660000));
		g.fillRect(10, 10, (WIDTH * 3) - 10, (HEIGHT * 3) - 10);
		g.setColor(new Color(0xFFFF9900));
		g.setFont(font.getArial());
		if (isGameOver()) {
			g.drawString("GAME OVER... What will you do now?", 35, 30);
		} else {
			String name = (Game.getJdata_UserName().length() >= 1) ? WordUtils
					.capitalizeFully(Game.getJdata_UserName()).toString()
					: "Player";
			g.drawString("Welcome to JavaGame " + name, 35, 30);
		}
		g.drawLine(10, (HEIGHT * 3), 10, 10);
		g.drawLine(10, 10, (WIDTH * 3), 10);
		g.drawLine((WIDTH * 3), 10, (WIDTH * 3), (HEIGHT * 3));
		g.drawLine(10, (HEIGHT * 3), (WIDTH * 3), (HEIGHT * 3));
		// (LEFT,DOWN,WIDTH,HEIGHT)
		paintButtons(isSelectedStart(), isSelectedExit(), g);
		bs.show();
		g.dispose();

	}

	private void paintButtons(boolean start, boolean exit, Graphics g) {
		// START
		if (!start) {
			g.setColor(new Color(0xFFBB4400));
			g.fillRect(35, 40, (frame.getWidth() - 67), 113);
			g.setColor(getDeSelected());
			g.fillRect(35, 40, (frame.getWidth() - 70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Start", 220, 95);
		} else {
			g.setColor(new Color(0xFFDD6600));
			g.fillRect(35, 40, (frame.getWidth() - 67), 113);
			g.setColor(getSelected());
			g.fillRect(35, 40, (frame.getWidth() - 70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Start", 220, 95);
		}
		// EXIT
		if (!exit) {
			g.setColor(new Color(0xFFBB4400));
			g.fillRect(35, 170, (frame.getWidth() - 67), 113);
			g.setColor(getDeSelected());
			g.fillRect(35, 170, (frame.getWidth() - 70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Exit", 220, 220);
		} else {
			g.setColor(new Color(0xFFDD6600));
			g.fillRect(35, 170, (frame.getWidth() - 67), 113);
			g.setColor(getSelected());
			g.fillRect(35, 170, (frame.getWidth() - 70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Exit", 220, 220);
		}
	}

	public static void play() {
		try {
			JSplash splash = new JSplash(
					Game.class.getResource("/splash/splash.png"), true, true,
					false, Game.getGameVersion(), null, Color.RED, Color.ORANGE);
			splash.toFront();
			splash.requestFocus();
			splash.splashOn();
			splash.setProgress(10, "Initializing Game");
			Thread.sleep(250);
			splash.setProgress(25, "Loading Classes");
			Thread.sleep(125);
			splash.setProgress(35, "Applying Configurations");
			Thread.sleep(125);
			splash.setProgress(40, "Loading Sprites");
			Thread.sleep(250);
			splash.setProgress(50, "Loading Textures");
			Thread.sleep(125);
			splash.setProgress(60, "Loading Map");
			Thread.sleep(500);
			splash.setProgress(80, "Configuring Map");
			Thread.sleep(125);
			splash.setProgress(90, "Pulling InputPanes");
			Thread.sleep(250);
			splash.setProgress(92, "Acquiring data: Multiplayer");
			Thread.sleep(125);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			String multiMsg = "Sorry but multiplayer has been disabled on this version.\nIf you would like multiplayer checkout Alpha 1.6";
			JOptionPane.showMessageDialog(Game.getGame(), multiMsg,
					"Multiplayer Warning", JOptionPane.WARNING_MESSAGE);
			// Game.setJdata_Host(JOptionPane.showConfirmDialog(Game.getGame(),
			// "Do you want to be the HOST?"));
			Game.setJdata_Host(1);
			if (Game.getJdata_Host() != 1) { // Game.getJdata_Host() == 1
				Game.setJdata_IP(JOptionPane.showInputDialog(Game.getGame(),
						"Enter the name \nleave blank for local"));
			}
			Thread.sleep(125);
			splash.setProgress(94, "Acquiring data: Username");
			String s = JOptionPane.showInputDialog(Game.getGame(),
					"Enter a name");
			if (s != null) {
				Game.setJdata_UserName(s);
			}
			Thread.sleep(125);
			splash.setProgress(96, "Collecting Player Data");
			Object[] options = { "African", "Caucasian" };
			int n = JOptionPane.showOptionDialog(frame,
					"Choose a race for the charater to be", "Choose a race",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[0]);
			if (n == 0) {
				Game.setAternateColsR(true);
			} else {
				Game.setAternateColsR(false);
			}
			Thread.sleep(250);
			Object[] options1 = { "Orange", "Black" };
			int n1 = JOptionPane.showOptionDialog(frame,
					"Which Colour do you want the shirt to be?",
					"Choose a shirt Colour", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options1, options1[0]);
			if (n1 == 0) {
				Game.setAternateColsS(true);
			} else {
				Game.setAternateColsS(false);
			}
			splash.setProgress(97, "Connecting as" + Game.getJdata_UserName());
			Thread.sleep(250);
			splash.splashOff();
			frame = new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME);
			frame.getFrame();
			frame.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DedicatedJFrame getFrame() {
		return Menu.frame;
	}

	public static void setFrame(DedicatedJFrame frame) {
		Menu.frame = frame;
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Menu.running = running;
	}

	public static boolean isSelectedStart() {
		return selectedStart;
	}

	public static void setSelectedStart(boolean selectedStart) {
		Menu.selectedStart = selectedStart;
	}

	public static boolean isSelectedExit() {
		return selectedExit;
	}

	public static void setSelectedExit(boolean selectedExit) {
		Menu.selectedExit = selectedExit;
	}

	public Color getSelected() {
		return selected;
	}

	public void setSelected(Color selected) {
		this.selected = selected;
	}

	public Color getDeSelected() {
		return deSelected;
	}

	public void setDeSelected(Color deSelected) {
		this.deSelected = deSelected;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static boolean isGameOver() {
		return gameOver;
	}

	public static void setGameOver(boolean gameOver) {
		Menu.gameOver = gameOver;
	}

}
