package com.foloke.starcorp.AI;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.foloke.starcorp.Entities.Ship;

public class ShipAgent implements Steerable<Vector2> {

    Ship ship;

    float linearSpeedThreshold = 0.00001f;
    float boundingRadius = 1f;
    boolean tagged = false;

    float maxLinearSpeed = 90f;
    float maxAngularSpeed = 1f;
    float maxLinearAcceleration = 1f;
    float maxAngularAcceleration = 1f;

    public float forwardForce = 10f;
    public float breakingForce = 5f;
    float rotationImpulse = 0.01f;
    float sidewaysForce = 5f;
    boolean dampingEnabled = true;

    SteeringBehavior<Vector2> steeringBehavior;
    SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    public ShipAgent(Ship ship) {
        this.ship = ship;
    }

    public void update(float delta) {
        if(steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            if(steeringOutput.linear.y > 0) {
               steeringOutput.linear.y *= forwardForce;
            } else {
                steeringOutput.linear.y *= breakingForce;
            }
            steeringOutput.linear.x *= sidewaysForce;

            steeringOutput.linear.rotateRad(getOrientation()).scl(delta * 60);

            steeringOutput.angular *= rotationImpulse * delta * 60;

            apply(steeringOutput, delta);
        }
    }

    public void toggleDamping() {
        dampingEnabled = !dampingEnabled;
    }

    public void apply(SteeringAcceleration<Vector2> steeringAcceleration, float delta) {
        if (ship.isActive()) {
            if (steeringAcceleration.linear.len() > 0) {
                ship.applyForce(steeringAcceleration.linear);
            } else {
                if (dampingEnabled) {
                    Vector2 vector2 = ship.getVelocityVec().cpy().rotateRad(-ship.getRotationRad() + 180 * MathUtils.degreesToRadians);

                    if (ship.getVelocity() > linearSpeedThreshold) {
                        Vector2 dampingForce = new Vector2();

                        float dampingX = MathUtils.clamp(vector2.x, -sidewaysForce, sidewaysForce);
                        float dampingY = MathUtils.clamp(vector2.y, -breakingForce, forwardForce);

                        dampingForce.x = dampingX;
                        dampingForce.y = dampingY;

                        ship.applyForce(dampingForce.rotateRad(ship.getRotationRad()).scl(60 * delta));
                    } else {
                        ship.forceStop();
                    }
                }
            }

            if (steeringAcceleration.angular != 0) {
                ship.applyAngular(steeringAcceleration.angular);
            }
        }

        steeringOutput.setZero();
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return ship.getVelocityVec();
    }

    @Override
    public float getAngularVelocity() {
        return ship.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return linearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.linearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return ship.getPos();
    }

    @Override
    public float getOrientation() {
        return ship.getRotationRad();
    }

    @Override
    public void setOrientation(float orientation) {
        ship.setRotationRad(orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override //TODO
    public Location<Vector2> newLocation() {
        return new SpaceLocation();
    }

}
