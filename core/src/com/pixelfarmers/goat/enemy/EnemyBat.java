package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class EnemyBat extends Enemy {

    private static final float BOUNDING_RADIUS = 8;
    protected Animation animation;

    float maxSpeed = 80;
    float maxAcceleration = 1000;
    private int hitPoints = 4;
    private int damage = 10;

    public EnemyBat(Vector2 startingPosition) {
        super(startingPosition);
        this.animation = AnimationLoader.getInstance().getAnimation("bat");
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(animation.getKeyFrame(stateTime, true), position.x - BOUNDING_RADIUS, position.y - BOUNDING_RADIUS);
    }

    @Override
    public boolean onHit(int damage) {
        hitPoints -= damage;
        return hitPoints <= 0;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxAcceleration;
    }

    @Override
    public float getBoundingRadius() {
        return BOUNDING_RADIUS;
    }

    @Override
    public int getDamage() {
        return damage;
    }

}
