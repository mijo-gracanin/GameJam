package com.pixelfarmers.goat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by mijo on 29/01/16.
 */
public class Player extends Sprite {


    public enum Movement {
        UP, DOWN, LEFT, RIGHT, STOP
    }

    public Movement movementDirection = Movement.UP;
    public float aimDirection = 0;

    private static final float COLLISION_RADIUS = 16f;
    private static final float MOVEMENT_SPEED = 4;
    private final Circle collisionCircle;


    public Player() {
        collisionCircle = new Circle(getX(), getY(), COLLISION_RADIUS);
    }

    public void update(float delta) {
        switch (movementDirection) {
            case UP: {
                setPosition(getX(), getY() + MOVEMENT_SPEED);
            }
            break;
            case DOWN: {
                setPosition(getX(), getY() - MOVEMENT_SPEED);
            }
            break;
            case LEFT: {
                setPosition(getX() - MOVEMENT_SPEED, getY());
            }
            break;
            case RIGHT: {
                setPosition(getX() + MOVEMENT_SPEED, getY());
            }
            break;
        }
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(getX(), getY(), collisionCircle.radius);
    }

}
