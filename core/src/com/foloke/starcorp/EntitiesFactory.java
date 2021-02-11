package com.foloke.starcorp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foloke.starcorp.Entities.Entity;
import com.foloke.starcorp.Entities.Ship;
import com.foloke.starcorp.packer.PEntity;
import com.foloke.starcorp.packer.PShip;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class EntitiesFactory {
    private static EntitiesFactory instance;
    private PShip[] ships = new PShip[]{};

    private EntitiesFactory() {

        Sprite sprite = new Sprite(new Texture("debug/debug_ship.png"));
        FileHandle coreShips = Gdx.files.internal("ships/core/core.json");

        TextureAtlas textureAtlas = new TextureAtlas("ships/core/core.atlas");

        ObjectMapper mapper = new ObjectMapper();
        try {
            ships = mapper.readValue(coreShips.reader(), PShip[].class);
        } catch (Exception e) {
            System.out.println(e);
        }

        for (PShip pShip: ships) {
            pShip.atlasRegion = textureAtlas.findRegion(pShip.texture);
        }
    }

    public Entity createEntity(int id) {
        //TODO: make datasheet
        Vector2[] vertices = new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(0.0f, 0.5f),
                new Vector2(0.5f, -0.5f),
                new Vector2(0f, 0f)
        };
        PShip pEntity = ships[id];

        return new Ship(pEntity);
    }

    public static EntitiesFactory getInstance() {
        if(instance == null) {
            instance = new EntitiesFactory();
        }
        return instance;
    }
}
