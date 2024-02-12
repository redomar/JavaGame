package com.redomar.game.gfx;

public class Colours {

	public static int get(int colour1, int colour2, int colour3, int colour4) {
		return (get(colour4) << 24) + (get(colour3) << 16) + (get(colour2) << 8) + (get(colour1));
	}

	private static int get(int colour) {

		if (colour < 0) {
			return 255;
		}

		int r = Math.min(Math.abs((colour / 100 % 10)), 5);
		int g = Math.min(Math.abs((colour / 10 % 10)), 5);
		int b = Math.min(Math.abs((colour % 10)), 5);

		return r * 36 + g * 6 + b;
	}

	public static int highlight(int colour) {

		if (colour < 0) {
			return 255;
		}

		int r = Math.min(Math.abs((colour / 100 % 10) + 1), 5);
		int g = Math.min(Math.abs((colour / 10 % 10) + 1), 5);
		int b = Math.min(Math.abs((colour % 10) + 1), 5);

		return r * 36 + g * 6 + b;
	}

	public static int[] getColours(int packedColours) {
		int[] colours = new int[4];

		// Extract each colour component
		colours[3] = reverseGet((packedColours >> 24) & 0xFF); // colour4
		colours[2] = reverseGet((packedColours >> 16) & 0xFF); // colour3
		colours[1] = reverseGet((packedColours >> 8) & 0xFF);  // colour2
		colours[0] = reverseGet(packedColours & 0xFF);         // colour1

		return colours;
	}

	private static int reverseGet(int colour) {
		if (colour == 255) {
			return -1; // Original colour was less than 0
		}

		int r = colour / 36;
		int g = (colour / 6) % 6;
		int b = colour % 6;

		return r * 100 + g * 10 + b;
	}

	public static int highlight(int colour, int colour1, int colour2, int colour3) {
		return (highlight(colour3) << 24) + (highlight(colour2) << 16) + (highlight(colour1) << 8) + (highlight(colour));
	}
}
