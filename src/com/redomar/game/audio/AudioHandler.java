package com.redomar.game.audio;

import javax.sound.sampled.*;
import java.io.File;


public class AudioHandler {

	private Clip clip;
	private boolean active = false;

	public AudioHandler(String path){
		initiate(path);
	}

	public AudioHandler(File file){
		try {
			if(file.toString() != ""){
				initiate(file.toString());
			} else {
				throw new NullPointerException();
			}
		} catch (NullPointerException e){
			System.err.println("Destination Cannot be empty");
			throw e;
		}
	}

	private void initiate(String path){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
			AudioFormat baseformat = audioInputStream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseformat.getSampleRate(), 16,
					baseformat.getChannels(),
					baseformat.getChannels() * 2,
					baseformat.getSampleRate(),
					false
			);
			AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(
					decodeFormat, audioInputStream);
			clip = AudioSystem.getClip();
			clip.open(decodedAudioInputStream);
		} catch (Exception e){
			System.err.println(e.getStackTrace());
		}
	}

	public void play(){
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
		active = true;
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
