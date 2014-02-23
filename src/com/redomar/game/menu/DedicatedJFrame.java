package com.redomar.game.menu;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class DedicatedJFrame extends Canvas {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	public DedicatedJFrame(int WIDTH, int HEIGHT, int SCALE, String NAME) {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));
		//getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().setLayout(new BorderLayout());
		getFrame().add(this, BorderLayout.CENTER);
		getFrame().pack();
		getFrame().setResizable(false);
		getFrame().setLocationRelativeTo(null);
		getFrame().setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		DedicatedJFrame.frame = frame;
	}

	public void stopFrame() {
		getFrame().dispose();
	}

}
