package com.redomar.game.lib;

import java.util.Random;

public class Name {

	private Random rand = new Random();
	private static int name_ID;

	public Name() {
		setRand(rand);
	}

	public String setName(String name) {

		String finalName = name + name_ID;
		return finalName;
	}

	public static void setRand(Random rand) {
		name_ID = rand.nextInt(300);
	}

	public static int getName_ID() {
		return name_ID;
	}
}
