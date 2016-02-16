package com.pixelfarmers.goat;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.pixelfarmers.goat.constants.Sounds;

//TODO menu music
public class MusicPlayer {

    private Music music;

    public MusicPlayer(AssetManager assetManager) {
        music = assetManager.get(Sounds.MUSIC, Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
    }

    public void play() {
        music.play();
    }

    public void stop() {
        music.stop();
    }

}
