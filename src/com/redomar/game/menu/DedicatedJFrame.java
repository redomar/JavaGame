package com.redomar.game.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class DedicatedJFrame {

	private static JFrame frame;

	public DedicatedJFrame(int WIDTH, int HEIGHT, int SCALE, String NAME){
		setFrame(new JFrame(NAME));
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().setLayout(new BorderLayout());
		getFrame().pack();
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);
		getFrame().setVisible(true);
		
		getFrame().setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		getFrame().setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		getFrame().setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		DedicatedJFrame.frame = frame;
	}
	
}
