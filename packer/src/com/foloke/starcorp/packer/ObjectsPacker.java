package com.foloke.starcorp.packer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ObjectsPacker {


    public static void main(String[] args) throws IOException {
        String source = "D:\\Packer\\core";
        String dst = "D:\\Packer\\packed";
        String packName = "core";
        File file = new File(dst + "\\" + packName);

        if(file.exists()) {
           delete(file);
        }

        TexturePacker.process(source, dst + "\\" + packName,packName);

        ObjectMapper mapper = new ObjectMapper();
        PShip pShip = new PShip("sub/debug_ship", new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(0.0f, 0.5f),
                new Vector2(0.5f, -0.5f),
                new Vector2(0f, 0f)}, 10, 5, 0.01f, 5);
        PShip pShip1 = new PShip("sub/debug_ship1", new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(-0.5f, 0.5f),
                new Vector2(0.5f, 0.5f),
                new Vector2(0.5f, -0.5f)}, 10, 5, 0.01f, 5);
        PShip pShip2 = new PShip("debug_ship2", new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(0.0f, 0.5f),
                new Vector2(0.5f, -0.5f),
                new Vector2(0f, 0f)}, 10, 5, 0.01f, 5);
        PShip pShip3 = new PShip("debug_ship3", new Vector2[]{
                new Vector2(-0.5f, -0.5f),
                new Vector2(0.0f, 0.5f),
                new Vector2(0.5f, -0.5f),
                new Vector2(0f, 0f)}, 10, 5, 0.01f, 5);

        PShip[] ships = new PShip[]{pShip, pShip1, pShip2, pShip3};
        mapper.writeValue(new File(dst + "\\" + packName + "\\" + packName + ".json"), ships);

        PShip[] debug = mapper.readValue(new File(dst + "\\" + packName + "\\" + packName + ".json"), PShip[].class);
    }

    private static void delete(File file) throws IOException {

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                delete(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }
}
