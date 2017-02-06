package org.usfirst.frc.team3310.utility;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
	private static Thread t;
	private static boolean run;
	
	public static void playSound(String fileName) {
		if(t == null) {
			run = true;
			t = new Thread("SoundPlayer") {
				@Override
				public void run() {
					if(!run) return;
					AudioInputStream ais;
					try {
						ais = AudioSystem.getAudioInputStream(new File(fileName));
						Clip c = AudioSystem.getClip();
						c.open(ais);
						c.start();
						run = false;
						Thread.sleep(c.getMicrosecondLength() / 1000);
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			};
			t.start();
		}
		else {
			run = true;
			t.run();
		}
	}
	
	/** Plays Dukes of Hazzard Horn */
	public static void play() {
		playSound("DukesOfHazzard.wav");
	}
}