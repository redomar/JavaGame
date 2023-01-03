package com.redomar.game.audio;

import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printer;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;


public class AudioHandler {

	private final Printer musicPrinter = new Printer(PrintTypes.MUSIC);
	private Clip clip;
	private boolean active = false;
	private boolean music = false;

	public AudioHandler(String path) {
		check(path);
	}

	public AudioHandler(@NotNull File file) {
		check(file.toString());
	}

	public AudioHandler(String path, boolean music) {
		this.music = music;
		check(path);
	}

	private void check(String path) {
		try {
			if (!path.equals("")) {
				initiate(path);
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			musicPrinter.print("Destination Cannot be empty", PrintTypes.ERROR);
			throw e;
		}
	}

	private void initiate(String path) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(AudioHandler.class.getResourceAsStream(path)));
			AudioFormat baseFormat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
		} catch (Exception e) {
			System.err.println(Arrays.toString(e.getStackTrace()));
			musicPrinter.print("Audio Failed to initiate", PrintTypes.ERROR);
		}
	}

	public void play() {
		try {
			if (clip == null) return;
			clip.setFramePosition(0);
			clip.start();
			if (music) musicPrinter.print("Playing Music");
			active = true;
		} catch (Exception e) {
			musicPrinter.print("Audio Failed to play", PrintTypes.ERROR);
			throw e;
		}
	}

	public void setVolume(float velocity) throws NullPointerException {
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(velocity);
	}

	public void stop() {
		if (clip.isRunning()) clip.stop();
		if (music) musicPrinter.print("Stopping Music");
		active = false;
	}

	public void close() {
		stop();
		clip.close();
	}

	public boolean getActive() {
		return this.active;
	}
}
