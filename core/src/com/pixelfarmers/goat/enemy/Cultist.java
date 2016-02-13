package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.util.AnimationLoader;

public class Cultist extends Enemy {

    private final Animation walkingAnimation;
    private final TextureRegion idleTexture;

    private int hitPoints = 2;

    public Cultist(Vector2 startingPosition) {
        super(startingPosition);
        this.walkingAnimation = AnimationLoader.getInstance().getAnimation("priest", 16, 32, 1);
        this.idleTexture = AnimationLoader.getInstance().getSingleTexture("priest", 16, 32, 0, 0);
    }

    @Override
    public boolean onHit(int damage) {
        hitPoints -= damage;
        return hitPoints <= 0;
    }

    @Override
    protected TextureRegion getTexture() {
        if(steeringBehavior != null) {
            return walkingAnimation.getKeyFrame(stateTime, true);
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
        return 12;
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
