package com.pixelfarmers.goat;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.constants.Sounds;

public class SoundPlayer implements Telegraph {

    AssetManager assetManager;

    public SoundPlayer(AssetManager assetManager) {
        this.assetManager = assetManager;
        MessageManager.getInstance().addListeners(this,
                MessageCode.PROJECTILE_HIT_ENEMY,
                MessageCode.SWORD_HIT_ENEMY,
                MessageCode.ENEMY_HIT_PLAYER,
                MessageCode.PROJECTILE_SHOOT,
                MessageCode.ON_WIN);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        int code = msg.message;
        switch (code) {
            case MessageCode.ENEMY_HIT_PLAYER:
                play(Sounds.PAIN);
                return true;
            case MessageCode.PROJECTILE_HIT_ENEMY:
                play(Sounds.PROJECTILE_HIT);
                return true;
            case MessageCode.SWORD_HIT_ENEMY:
                play(Sounds.SWORD_HIT);
                return true;
            case MessageCode.PROJECTILE_SHOOT:
                play(Sounds.PROJECTILE_SHOOT);
                return true;
            case MessageCode.ON_WIN:
                play(Sounds.FADEOUT_NOISE, 0.5f);
            default:
                return false;
        }
    }

    private void play(String id, float volume) {
        getSound(id).play(volume);
    }

    private void play(String id) {
        getSound(id).play();
}

    private Sound getSound(String id) {
        return assetManager.get(id, Sound.class);
    }

}
