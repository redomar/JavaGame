package com.redomar.game.audio;

import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AudioHandler {

	public static final Printer musicPrinter = new Printer(PrintTypes.MUSIC);
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
			if (!path.isEmpty()) {
				initiate(path);
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			musicPrinter.print("Destination Cannot be empty", PrintTypes.ERROR);
			throw e;
		}
	}

	/**
	 * Initialises an audio clip by loading an audio file from the specified path. This method sets up the audio stream and prepares the clip for playback.
	 *
	 * @param path the relative file path to the audio clip resource. The path must be accessible from the classpath and should not be null.
	 */
	private void initiate(@NotNull String path) {
		try {
			InputStream inputStream = new BufferedInputStream(Objects.requireNonNull(AudioHandler.class.getResourceAsStream(path)));
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
			audioInputStream.mark(Integer.MAX_VALUE);
			AudioFormat baseFormat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
			clip.addLineListener(event -> {
				if (event.getType() == LineEvent.Type.STOP) {
					stop();
				}
			});

		} catch (IOException e) {
			musicPrinter.cast().exception("Audio file not found " + path);
			musicPrinter.cast().exception(e.getMessage());

		} catch (Exception e) {
			musicPrinter.print("Audio Failed to initiate", PrintTypes.ERROR);
			musicPrinter.cast().exception(e.getMessage());
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
		try {
			if (clip == null) throw new RuntimeException("Empty clip");
			if (clip.isRunning()) {
				clip.stop();
				if (!music) clip.close();
			}
			if (music & active) musicPrinter.print("Stopping Music");
		} catch (Exception e) {
			musicPrinter.print("Audio Handler Clip not found");
		} finally {
			active = false;
		}
	}

	public void close() {
		stop();
		clip.close();
	}

	public boolean getActive() {
		return this.active;
	}
}
