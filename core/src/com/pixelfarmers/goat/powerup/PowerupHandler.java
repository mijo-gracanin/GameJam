package com.pixelfarmers.goat.powerup;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.MessageCode;

public class PowerupHandler implements Telegraph {

    private static final float HEALTH_CHANCE = 0.1f;
    private static final float SPEED_CHANCE = 0.2f;
    private static final float DMG_UP_CHANCE = 0.3f;
    private AssetManager assetManager;

    public PowerupHandler(AssetManager assetManager) {
        this.assetManager = assetManager;
        MessageManager.getInstance().addListener(this, MessageCode.ENEMY_DIED);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if(msg.message == MessageCode.ENEMY_DIED) {
            float random = MathUtils.random();
            if(random < DMG_UP_CHANCE) {
                Vector2 position = (Vector2) msg.extraInfo;
                createPowerup(position, random);
            }

            return true;
        }
        return false;
    }

    private void createPowerup(Vector2 position, float random) {
        Powerup.TYPE type;
        if(random < HEALTH_CHANCE) {
            type = Powerup.TYPE.HEALTH;
        } else if(random < SPEED_CHANCE) {
            type = Powerup.TYPE.SPEED;
        } else {
            type = Powerup.TYPE.DAMAGE;
        }
        Powerup powerup = new Powerup(position, assetManager.get(type.textureName, Texture.class), type);
        MessageManager.getInstance().dispatchMessage(0, MessageCode.POWERUP_ADDED, powerup);
    }

}
