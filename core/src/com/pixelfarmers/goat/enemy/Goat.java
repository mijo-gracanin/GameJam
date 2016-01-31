package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.MessageCode;

public class Goat extends Enemy implements Telegraph {

    private final Animation animation;
    private final TextureRegion idleTexture;

    private boolean isFollowingPlayer = false;

    public Goat(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("goat", 32, 32, 0);
        this.idleTexture = AnimationLoader.getInstance().getSingleTexture("goat_sitting", 32, 32, 0, 0);
        MessageManager.getInstance().addListener(this, MessageCode.GOAT_START_FOLLOW);
    }

    @Override
    public void update(float delta) {
        if(!isFollowingPlayer) {
            //Nothing?
        } else {
            super.update(delta);
        }
    }

    @Override
    public boolean onHit(int damage) {
        return false;
    }

    @Override
    protected TextureRegion getTexture() {
        return getCurrentTexture();
    }

    private TextureRegion getCurrentTexture() {
        if (isFollowingPlayer) {
            TextureRegion textureRegion = animation.getKeyFrame(stateTime, true);
            if (linearVelocity.x > 0 && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if (linearVelocity.x < 0 && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            return textureRegion;
        } else {
            return idleTexture;
        }
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getMaxLinearSpeed() {
        return 60;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 1000;
    }

    @Override
    public float getBoundingRadius() {
        return 8;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if(msg.message == MessageCode.GOAT_START_FOLLOW) {
            isFollowingPlayer = true;
            return true;
        }
        return false;
    }

}
