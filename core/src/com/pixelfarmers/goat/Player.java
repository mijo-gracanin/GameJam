package com.pixelfarmers.goat;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by mijo on 29/01/16.
 */
public class Player {


    public enum Movement {
        UP, DOWN, LEFT, RIGHT, STOP
    }

    public Movement movementDirection = Movement.UP;
    public float aimDirection = 0;

    private static final float COLLISION_RADIUS = 16f;
    private static final float MOVEMENT_SPEED = 4;
    private final Circle collisionCircle;

    private float x = 0;
    private float y = 0;

    public Player() {
        collisionCircle = new Circle(x,y, COLLISION_RADIUS);
    }

    public void update(float delta) {
        switch (movementDirection) {
            case UP: {
                y += MOVEMENT_SPEED;
            }
            break;
            case DOWN: {
                y -= MOVEMENT_SPEED;
            }
            break;
            case LEFT: {
                x -= MOVEMENT_SPEED;
            }
            break;
            case RIGHT: {
                x += MOVEMENT_SPEED;
            }
            break;
        }
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(x, y, collisionCircle.radius);
    }



}
