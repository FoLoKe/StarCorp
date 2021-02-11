package com.foloke.starcorp.packer;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PEntity {
    public String texture;
    public Vector2[] vertices;

    @JsonIgnore
    public TextureAtlas.AtlasRegion atlasRegion;

    public PEntity() {}
    public PEntity(String textureName, Vector2[] vertices) {
        this.texture = textureName;
        this.vertices = vertices;
    }

    public float[][] getVertices() {
        float[][] vertices = new float[this.vertices.length][2];
        for (int i = 0; i < this.vertices.length; i++) {
            vertices[i][0] = this.vertices[i].x;
            vertices[i][1] = this.vertices[i].y;
        }
        return vertices;
    }

    public void setVertices(float[][] vertices) {
        this.vertices = new Vector2[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            this.vertices[i] = new Vector2(vertices[i][0], vertices[i][1]);
        }
    }
}
