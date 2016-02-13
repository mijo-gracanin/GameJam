package com.pixelfarmers.goat.player;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Projectile implements PhysicalEntity {

    private static final float SPEED_MODIFIER = 100f;
    private static final float COLLISION_RADIUS = 3f;
    private float orientationInRadians = 0;
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2(1 * SPEED_MODIFIER, 1 * SPEED_MODIFIER);
    private Circle collisionCircle = new Circle(position, COLLISION_RADIUS);
    private Texture texture;

    private int damage = 2;

    public Projectile(Texture texture, Vector2 position, Vector2 shooterVelocity, float orientationInRadians) {
        this.texture = texture;
        this.position = position.cpy();
        this.orientationInRadians = orientationInRadians;
        velocity.add(new Vector2(Math.abs(shooterVelocity.x), Math.abs(shooterVelocity.y)));
    }

    public void update(float delta) {
        velocity.setAngleRad(orientationInRadians);
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        collisionCircle.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        //shapeRenderer.circle(position.x, position.y, COLLISION_RADIUS);
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientationInRadians = orientation;
    }

    @Override
    public float getOrientation() {
        return orientationInRadians;
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
        return position;
    }

    @Override
    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public int getDamage() {
        return damage;
    }
}
