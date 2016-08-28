package com.redomar.game.audio;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Mohamed on 28/08/2016.
 * This file tests the com.redomar.game.audio class
 */
public class AudioEffectTest {

	@Test
	public void playSoundEffectAndSetVolume() {
		AudioHandler sound = new AudioHandler("/sfx/smallProjectile.wav");
		sound.setVolume(-15);
		sound.play();
	}

	@Test
	public void sfxFileExists() throws Exception {
		File sfx = new File("res/sfx/smallProjectile.wav");
		assertTrue(sfx.exists());
	}

}