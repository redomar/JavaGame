package com.redomar.game.objects;

import com.redomar.game.Game;

public class Inventory {

	public static int x;
	public static int y;
	public static boolean open = false;
	public static boolean closing;
	public static boolean reset;
	public static boolean enabled;
	private static final InventoryWindow inv_window = new InventoryWindow();

	public static void activate() {
		x = (int) Game.getPlayer().getX();
		y = (int) Game.getPlayer().getY();

		if (Game.getLevel().getTile(x >> 3, y >> 3).getId() == 8) {
			if (enabled) {
				if (!open) {
					if (!closing) {
						System.out.println("Opened\nInside this Bag their is:" + inside());
						open = true;
						Game.getPlayer().setMoving(false);
						inv_window.start();
					}
				} else {
					if (closing) {
						Game.getPlayer().setMoving(true);
						inv_window.stop();
						inv_window.getFrame().setVisible(false);
						inv_window.getFrame().stopFrame();
						if (Game.getLevel().getTile(x >> 3, y >> 3).getId() == 8) {
							reset = true;
							System.out.println("rest");
						}
					}
				}
			}
		} else {
			if (open || reset || closing) {
				reset = false;
				open = false;
				closing = false;
			}
		}
	}

	private static String inside() {
		String items = " ";
		for (Items item : Items.values()) {
			items = String.format("%s%s, ", items, item.toString());
		}
		return items;
	}
}
