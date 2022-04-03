package com.pacman.entity;

import javax.sound.sampled.*;
import java.io.File;


public class Sound {
    Clip clip;
    File[] files;

    public enum MenuSound {
        Start,
        Loading,
        Music
    }

    public enum PacmanSound {
        Wakawaka,
        Death,
        EatEnergizer
    }

    public enum GhostSound {
        Normal,
        Frightened,
        Eaten
    }

    public Sound() {
        files = new File[10];
        files[0] = new File("src/com/pacman/res/Sounds/MenuSound.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(files[0]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
