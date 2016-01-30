package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends SteerableAdapter<Vector2> {

    private static final float BOUNDING_RADIUS = 8;

    protected SteeringBehavior<Vector2> steeringBehavior;
    protected Texture texture;
    private static final SteeringAcceleration<Vector2> steeringOutput =
            new SteeringAcceleration<Vector2>(new Vector2());

    Vector2 position;
    float orientation;
    Vector2 linearVelocity = new Vector2(0, 0);
    float angularVelocity = 0;
    float maxSpeed = 60;
    float maxAcceleration = 1000;

    public Enemy(Texture texture, Vector2 startingPosition) {
        this.texture = texture;
        this.position = new Vector2(startingPosition.x, startingPosition.y);
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    public void update(float delta) {
        steeringBehavior.calculateSteering(steeringOutput);
        applySteering(steeringOutput, delta);
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        this.position.mulAdd(linearVelocity, time);
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());
        this.orientation += angularVelocity * time;
        this.angularVelocity += steering.angular * time;
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxSpeed;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxAcceleration;
    }

    @Override
    public float getBoundingRadius() {
        return BOUNDING_RADIUS;
    }
}
