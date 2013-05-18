package com.redomar.game;

import java.applet.Applet;
import java.awt.BorderLayout;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet{

	private static Game game = new Game();
	private static final boolean debug = true;
	
	@Override
	public void init(){
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		setMaximumSize(Game.DIMENSIONS);
		setMinimumSize(Game.DIMENSIONS);
		setPreferredSize(Game.DIMENSIONS);
	}
	
	@Override
	public void start(){
		game.start();
	}
	
	@Override
	public void stop(){
		game.stop();
	}
	
	public static void main(String[] args) {
		game.setMinimumSize(Game.DIMENSIONS);
		game.setMaximumSize(Game.DIMENSIONS);
		game.setPreferredSize(Game.DIMENSIONS);

		game.setFrame(new JFrame(Game.NAME));
		game.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.getFrame().setLayout(new BorderLayout());
		game.getFrame().add(game, BorderLayout.CENTER);
		game.getFrame().pack();
		game.getFrame().setResizable(false);
		game.getFrame().setLocationRelativeTo(null);
		game.getFrame().setVisible(true);
		game.setWindow(new WindowHandler(game));
		game.setDebug(debug);

		game.start();
	}
	
}
