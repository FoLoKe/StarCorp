package com.foloke.starcorp.Entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.foloke.starcorp.AI.ShipAgent;
import com.foloke.starcorp.packer.PShip;

public class Ship extends Entity {

    public ShipAgent shipAgent;

    public Ship(PShip pShip) {
        super(pShip);
        shipAgent = new ShipAgent(this);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
//
    }

    public float getVelocity() {
        if(active) {
            return body.getLinearVelocity().len();
        }
        return 0f;
    }

    public Vector2 getVelocityVec() {
        if(active) {
            return body.getLinearVelocity();
        }
        return new Vector2();
    }

    public float getAngularVelocity() {
        if(active) {
            return body.getAngularVelocity();
        }
        return 0f;
    }

    public float getRotationRad() {
        if(active) {
            return body.getAngle();
        }
        return 0f;
    }

    public void setDamping(float damping) {
        body.setLinearDamping(damping);
    }

    public void applyForce(Vector2 vector2) {
        body.applyForceToCenter(vector2, true);
    }

    public void applyAngular(float angular) {
        body.applyAngularImpulse(angular, true);
    }

    @Override
    protected void createBody(Vector2[] vertices, World world) {
        super.createBody(vertices, world);
        body.setLinearDamping(0);
    }
}
