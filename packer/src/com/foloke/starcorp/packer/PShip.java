package com.foloke.starcorp.packer;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PShip extends PEntity {
    public float forwardForce = 10f;
    public float breakingForce = 5f;
    float rotationImpulse = 0.01f;
    float sidewaysForce = 5f;

    public PShip() {}
    public PShip(String textureName, Vector2[] vertices,
                 float forwardForce, float breakingForce, float sidewaysForce, float rotationImpulse) {
        super(textureName, vertices);
        this.forwardForce = forwardForce;
        this.breakingForce = breakingForce;
        this.sidewaysForce = sidewaysForce;
        this.rotationImpulse =rotationImpulse;
    }
}
