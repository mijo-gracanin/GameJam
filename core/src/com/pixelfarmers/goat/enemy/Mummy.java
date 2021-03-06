package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.util.AnimationLoader;

public class Mummy extends Enemy {

    protected Animation animation;
    private int hitPoints = 4;

    public Mummy(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("mummy", 16, 32, 0);
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(getTexture(), position.x - getBoundingRadius(), position.y - (getBoundingRadius() * 2));
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
        return 30;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 800;
    }

    @Override
    public float getBoundingRadius() {
        return 8;
    }

    @Override
    public int getDamage() {
        return 2;
    }
}
