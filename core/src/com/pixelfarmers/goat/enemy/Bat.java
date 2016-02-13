package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.util.AnimationLoader;

public class Bat extends Enemy {

    protected Animation animation;
    private int hitPoints = 4;

    public Bat(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("bat", 32, 16, 0);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(getTexture(), position.x - (getBoundingRadius() * 2), position.y - getBoundingRadius());
    }

    @Override
    protected TextureRegion getTexture() {
        return animation.getKeyFrame(stateTime, true);
    }

    @Override
    public boolean onHit(int damage) {
        hitPoints -= damage;
        return hitPoints <= 0;
    }

    @Override
    public float getMaxLinearSpeed() {
        return 80;
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
    public int getDamage() {
        return 1;
    }

}
