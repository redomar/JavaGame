package com.redomar.game.audio;

import com.redomar.game.script.PrintTypes;
import com.redomar.game.script.Printing;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Arrays;


public class AudioHandler {

	private Clip clip;
	private boolean active = false;
	private final Printing p = new Printing();

	public AudioHandler(String path){
		check(path);
	}

	public AudioHandler(File file){
		check(file.toString());
	}

	private void check(String path){
		try {
			if(!path.equals("")){
				initiate(path);
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e){
			p.print("Destination Cannot be empty", PrintTypes.ERROR);
			throw e;
		}
	}

	private void initiate(String path){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(AudioHandler.class.getResourceAsStream(path));
			AudioFormat baseFormat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
			);
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(
					decodeFormat, audioInputStream);
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
		} catch (Exception e){
			System.err.println(Arrays.toString(e.getStackTrace()));
			p.print("Audio Failed to initiate", PrintTypes.ERROR);
		}
	}

	public void play(){
		try{
			if(clip == null) return;
			stop();
			clip.setFramePosition(0);
			clip.start();
			active = true;
		} catch (Exception e) {
			p.print("Audio Failed to play", PrintTypes.ERROR);
			throw e;
		}
	}

	public void setVolume(float velocity){
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(velocity);

	}

	public void stop() {
		if (clip.isRunning()) clip.stop();
		active = false;
	}

	public void close(){
		stop();
		clip.close();
	}

	public boolean getActive(){
		return this.active;
	}

}
