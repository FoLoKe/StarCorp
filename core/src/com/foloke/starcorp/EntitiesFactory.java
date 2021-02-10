package com.foloke.starcorp;

import com.badlogic.gdx.math.Vector2;
import com.foloke.starcorp.Entities.Entity;
import com.foloke.starcorp.Entities.Ship;

public class EntitiesFactory {
    private EntitiesFactory() {

    }

    public static Entity createEntity(int id) {
        //TODO: make datasheet
        Vector2[] vertices = new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(0.0f, 0.5f),
                new Vector2(0.5f, -0.5f),
                new Vector2(0f, 0f)
        };
        if(id == 0) {
            return new Entity(vertices);
        }
        return new Ship(vertices);
    }

    public static Entity createEntity(int id, Vector2 vector2, float degrees) {

        Entity entity = createEntity(id);
        entity.setPosition(vector2);
        entity.setRotationDeg(degrees);

        return entity;
    }
}
