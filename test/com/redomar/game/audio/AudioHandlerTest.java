package com.redomar.game.audio;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * Created by Mohamed on 28/08/2016.
 * This file tests the com.redomar.game.audio.AudioHandler class
 */
public class AudioHandlerTest {

	@Before
	public void before() {
		AudioHandler.musicPrinter.mute();
	}

	@Test
	public void bgMusicExists() {
		File sfx = new File("res/music/Towards The End.mp3");
		assertTrue(sfx.exists());
	}

	@Test(expected = NullPointerException.class)
	public void expectReturnExceptionFileEmptyDir() {
		File empty = new File("");
		new AudioHandler(empty);
	}

	@Test(expected = NullPointerException.class)
	public void expectReturnExceptionFileEmptyPath() {
		new AudioHandler("");
	}

	@Test()
	public void tryInitiatingAndPlayingNonExistingFile() {
		new AudioHandler("//").play();
	}

}