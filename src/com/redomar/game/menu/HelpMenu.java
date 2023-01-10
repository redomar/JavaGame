package com.redomar.game.menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Credit to Gagandeep Bali @ stackoverflow
 */
public class HelpMenu {

	private final JFrame frame = new JFrame("Help Menu");

	private static void run() {
		Runnable runnable = () -> new HelpMenu().displayGUI();
		EventQueue.invokeLater(runnable);
	}

	private void displayGUI() {
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		MyPanel contentPane = new MyPanel();

		frame.setContentPane(contentPane);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	public void helpMenuLaunch() {
		run();
	}

	public void helpMenuClose() {
		frame.setVisible(false);
		frame.dispose();
	}

	private static class MyPanel extends JPanel {

		private BufferedImage image;

		private MyPanel() {
			try {
				AtomicReference<URL> controlsImageResource = new AtomicReference<>(MyPanel.class.getResource("/controls/controls.png"));
				image = ImageIO.read(controlsImageResource.get());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return image == null ? new Dimension(400, 300) : new Dimension(image.getWidth(), image.getHeight());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}
}
