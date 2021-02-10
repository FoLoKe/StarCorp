package com.foloke.starcorp.Entities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public class ManualSteering extends SteeringBehavior<Vector2> {

    private boolean forward, backward, left, right, strafeLeft, strafeRight;
    public ManualSteering(Steerable<Vector2> owner) {
        super(owner);
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {

        owner.getMaxLinearAcceleration();
        if(forward) {
            steering.linear.add(0, owner.getMaxLinearAcceleration());
        } else if (backward) {
            steering.linear.add(0, -owner.getMaxLinearAcceleration());
        }

        if(strafeLeft) {
            steering.linear.add(-owner.getMaxLinearAcceleration(), 0);
        } else if (strafeRight) {
            steering.linear.add(owner.getMaxLinearAcceleration(), 0);
        }

        if(left) {
            steering.angular += owner.getMaxAngularAcceleration();
        } else if (right) {
            steering.angular -= owner.getMaxAngularAcceleration();
        }

        steering.linear.nor();
        forward = backward = strafeLeft = strafeRight = left = right = false;
        return steering;
    }

    public void forward() {
        forward = true;
    }

    public void backward() {
        backward = true;
    }

    public void left() {
        left = true;
    }

    public void right() {
        right = true;
    }

    public void strafeLeft() {
        strafeLeft = true;
    }

    public void strafeRight() {
        strafeRight = true;
    }
}
