package com.pixelfarmers.goat.player;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.constants.Textures;

/**
 * Created by mijo on 30/01/16.
 */
public class Sword implements PhysicalEntity {

    private boolean isActive = false;
    private static final float STRETCHING_SPEED = 90f;
    private static final float MAX_SWORD_LENGTH = 22;
    private static final float COLLISION_RADIUS = 3f;
    private static final int SWORD_TEXTURE_WIDTH = 5;
    private Vector2 position = new Vector2();
    private Circle collisionCircle = new Circle(position, COLLISION_RADIUS);
    private boolean isGrowing = true;
    private float swordLength = 0;
    private float orientation = 0;
    private Texture swordTexture;


    public Sword(Vector2 position, AssetManager assetManager) {
        this.position = position.cpy();
        swordTexture = assetManager.get(Textures.SWORD, Texture.class);
    }

    public void update(Vector2 playerPosition, float playerOrientation, float delta) {
        if (!isActive()) return;

        Vector2 speedVector = new Vector2(1, 1);
        speedVector.setAngleRad(playerOrientation);
        position = playerPosition.cpy();
        position.y += 8;
        orientation = playerOrientation;

        float direction = isGrowing ? 1 : -1;
        swordLength += STRETCHING_SPEED * delta * direction;
        float pointyEndX = speedVector.x * swordLength + position.x ;
        float pointyEndY = speedVector.y * swordLength + position.y;
        collisionCircle.setPosition(pointyEndX, pointyEndY);

        if (swordLength > MAX_SWORD_LENGTH) {
            isGrowing = false;
        }
        else if (swordLength < 0.1 && !isGrowing) {
            hideSword();
        }
    }

    public void draw(Batch batch) {
        if (!isActive()) return;
        batch.draw(swordTexture, position.x, position.y,
                0, 0,
                SWORD_TEXTURE_WIDTH, swordLength,
                0.8f, 1.3f,
                (orientation - (MathUtils.PI / 2)) * MathUtils.radDeg,
                0, (int)(MAX_SWORD_LENGTH - swordLength),
                SWORD_TEXTURE_WIDTH, (int)swordLength, false, false);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        if (!isActive()) return;
        //shapeRenderer.circle(collisionCircle.x, collisionCircle.y, COLLISION_RADIUS);
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return null;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(collisionCircle.x, collisionCircle.y);
    }

    @Override
    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public boolean isActive() {
        return isActive;
    }

    private void hideSword() {
        isActive = false;
        isGrowing = false;
        swordLength = 0;
    }

    public void cast(Vector2 startPosition) {
        position = startPosition.cpy();
        isActive = true;
        isGrowing = true;
    }

    public int getDamage() {
        return 3;
    }
}
