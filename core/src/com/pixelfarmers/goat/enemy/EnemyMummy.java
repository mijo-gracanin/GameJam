package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyMummy extends Enemy {
    protected Animation animation;
    private int hitPoints = 4;

    public EnemyMummy(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("mummy", 16, 32);
    }

    @Override
    protected TextureRegion getTexure() {
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
        return 10;
    }
}
