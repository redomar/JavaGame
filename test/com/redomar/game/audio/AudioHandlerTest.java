package com.redomar.game.audio;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Mohamed on 28/08/2016.
 * This file tests the com.redomar.game.audio.AudioHandler class
 */
public class AudioHandlerTest {

	@Test
	public void bgMusicExists() throws Exception {
		File sfx = new File("res/music/Towards The End.mp3");
		assertTrue(sfx.exists());
	}

	@Test(expected = NullPointerException.class)
	public void expectReturnExceptionFileEmptyDir(){
		File empty = new File("");
		AudioHandler audio = new AudioHandler(empty);
	}

	@Test(expected = NullPointerException.class)
	public void expectReturnExceptionFileEmptyPath(){
		AudioHandler audio = new AudioHandler("");
	}

	@Test
	public void tryInitiatingAndPlayingNonExistingFile(){
		AudioHandler audio = new AudioHandler("//");
		audio.play();
	}

}