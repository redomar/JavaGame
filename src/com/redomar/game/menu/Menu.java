package com.redomar.game.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.redomar.game.Game;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.Mouse;
import com.thehowtotutorial.splashscreen.JSplash;



public class Menu implements Runnable{

	private static final String game_Version = "v1.5.3 Alpha";
	private static final int WIDTH = 160;
	private static final int HEIGHT = (WIDTH / 3 * 2);
	private static final int SCALE = 3;
	private static final String NAME = "Menu";
	
	private static boolean running = false;
	private static boolean selectedStart = false;
	private static boolean selectedExit = false;
	private static boolean gameOver = false;
	
	private static DedicatedJFrame frame;// = new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME);
	private Font font = new Font();
	private MouseListener Mouse = new Mouse();
	
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
				frame.getFrame().setTitle("Frames: " + frames + " Ticks: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}

	private void render() {
		//frame.getFrame().getContentPane().setBackground(Color.GREEN);
		frame.addMouseMotionListener((MouseMotionListener) Mouse);
		frame.addMouseListener(Mouse);
		BufferStrategy bs = frame.getBufferStrategy();
		if (bs == null) {
			frame.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(new Color(0xFF660000));
		g.fillRect(10, 10, (WIDTH*3)-10, (HEIGHT*3)-10);
		g.setColor(new Color(0xFFFF9900));
		g.setFont(font.getArial());
		if(isGameOver()){
			g.drawString("GAME OVER... What will you do now?",35,30);			
		}else{
			g.drawString("Welcome to JavaGame",35,30);
		}
		g.drawLine(10, (HEIGHT*3), 10, 10);
		g.drawLine(10, 10, (WIDTH*3), 10);
		g.drawLine((WIDTH*3), 10, (WIDTH*3), (HEIGHT*3));
		g.drawLine(10, (HEIGHT*3), (WIDTH*3), (HEIGHT*3));
		//(LEFT,DOWN,WIDTH,HEIGHT)
		if (isSelectedStart() == true){
			//START
			g.setColor(getSelected());
			g.fillRect(35, 40, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Start", 220, 95);
			//EXIT
			g.setColor(getDeSelected());
			g.fillRect(35, 170, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Exit", 220, 220);
		} else if (isSelectedExit() == true){
			//START
			g.setColor(getDeSelected());
			g.fillRect(35, 40, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Start", 220, 95);
			//EXIT
			g.setColor(getSelected());
			g.fillRect(35, 170, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Exit", 220, 220);
		}else{
			//START
			g.setColor(getDeSelected());
			g.fillRect(35, 40, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Start", 220, 95);
			//EXIT
			g.setColor(getDeSelected());
			g.fillRect(35, 170, (frame.getWidth()-70), 110);
			g.setColor(Color.BLACK);
			g.drawString("Exit", 220, 220);
		}
		bs.show();
		g.dispose();
		
	}

	public static void main(String[] args) {
		new Menu().start();
	}
	
	public static void play(){
		try {
			JSplash splash = new JSplash(Game.class.getResource("/splash/splash.png"), true, true, false, game_Version, null, Color.RED, Color.ORANGE);
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
			splash.setProgress(92, "Aquring data: Multiplayer");
			Thread.sleep(125);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Game.setJdata_Host(JOptionPane.showConfirmDialog(Game.getGame(), "Do you want to be the HOST?"));
			if (Game.getJdata_Host() == 1){
				Game.setJdata_IP(JOptionPane.showInputDialog(Game.getGame(), "Enter the name \nleave blank for local"));
			}
			Thread.sleep(125);
			splash.setProgress(95, "Aquring data: Username");
			Thread.sleep(125);
			splash.setProgress(96, "Initalizing as Server:Host");
			Game.setJdata_UserName(JOptionPane.showInputDialog(Game.getGame(), "Enter a name"));
			splash.setProgress(97, "Connecting as" + Game.getJdata_UserName());
			Thread.sleep(250);
			splash.splashOff();
			frame = new DedicatedJFrame(WIDTH, HEIGHT, SCALE, NAME);
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
