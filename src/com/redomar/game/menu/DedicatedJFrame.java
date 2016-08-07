package com.redomar.game.menu;

import javax.swing.*;
import java.awt.*;

public class DedicatedJFrame extends Canvas {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	public DedicatedJFrame(int WIDTH, int HEIGHT, int SCALE, String NAME) {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		setFrame(new JFrame(NAME));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static JFrame getFrameStatic() {
		return frame;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		DedicatedJFrame.frame = frame;
	}

	public void stopFrame() {
		frame.dispose();
	}

}
