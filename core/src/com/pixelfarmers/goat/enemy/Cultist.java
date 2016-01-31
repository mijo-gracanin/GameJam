package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Cultist extends Enemy {

    private final Animation animation;

    private int hitPoints = 2;

    public Cultist(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("priest", 16, 32, 1);
    }

    @Override
    public boolean onHit(int damage) {
        hitPoints -= damage;
        return hitPoints <= 0;
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

}
