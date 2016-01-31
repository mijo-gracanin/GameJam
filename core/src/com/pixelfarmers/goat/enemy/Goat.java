package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Goat extends Enemy {

    private final Animation animation;

    public Goat(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("priest", 16, 32);
    }

    @Override
    public boolean onHit(int damage) {
        return false;
    }

    @Override
    protected TextureRegion getTexture() {
        return animation.getKeyFrame(stateTime, true);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getMaxLinearSpeed() {
        return 40;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 800;
    }

    @Override
    public float getBoundingRadius() {
        return 8;
    }

}
