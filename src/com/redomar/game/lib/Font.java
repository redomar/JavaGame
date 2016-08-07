package com.redomar.game.lib;

import com.redomar.game.gfx.Screen;

public class Font {

	private static java.awt.Font arial;
	private static java.awt.Font segoe;
	private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      "
			+ "0123456789.,:;'\"!?$%()-=+/      ";

	public Font() {
		Font.setArial(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
		Font.setSegoe(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
	}

	public static void render(String msg, Screen screen, int x, int y,
							  int colour, int scale) {
		msg = msg.toUpperCase();

		for (int i = 0; i < msg.length(); i++) {
			int charIndex = chars.indexOf(msg.charAt(i));
			if (charIndex >= 0) {
				screen.render(x + (i * 8), y, charIndex + 30 * 32, colour,
						0x00, scale);
			}
		}
	}

	public java.awt.Font getArial() {
		return arial;
	}

	public static void setArial(java.awt.Font arial) {
		Font.arial = arial;
	}

	public java.awt.Font getSegoe() {
		return segoe;
	}

	public static void setSegoe(java.awt.Font segoe) {
		Font.segoe = segoe;
	}

}
