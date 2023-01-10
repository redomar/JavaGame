package com.redomar.game.lib;

import java.util.Random;

@Deprecated
public class Name {

	private static int name_ID;

	public Name() {
		Random rand = new Random();
		setRand(rand);
	}

	@Deprecated
	public String setName(String name) {

		return name + name_ID;
	}

	public static void setRand(Random rand) {
		name_ID = rand.nextInt(300);
	}

	public static int getName_ID() {
		return name_ID;
	}
}
