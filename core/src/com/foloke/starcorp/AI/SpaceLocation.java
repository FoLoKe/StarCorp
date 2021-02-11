package com.foloke.starcorp.AI;


import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class SpaceLocation implements Location<Vector2> {
    private Vector2 location;
    private float orient;

    public SpaceLocation() {

    }

    public SpaceLocation(Vector2 location, float orient) {
        super();
        this.location = location.cpy();
        this.orient = orient;
    }

    @Override
    public Vector2 getPosition() {
        return location;
    }

    @Override
    public float getOrientation() {
        return orient;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orient = orientation;
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

    @Override
    public Location<Vector2> newLocation() {
        return new SpaceLocation();
    }
}
