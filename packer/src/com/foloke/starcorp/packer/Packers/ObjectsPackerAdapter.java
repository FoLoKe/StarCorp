package com.foloke.starcorp.packer.Packers;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foloke.starcorp.packer.PShip;

import java.io.File;
import java.io.IOException;

public class ObjectsPackerAdapter<T> implements ObjectsPacker {

    private final Class<T[]> runtimeType;
    public ObjectsPackerAdapter(Class<T[]> runtimeType) {
        this.runtimeType = runtimeType;
    }
    @Override
    public void pack(File src, File dst) throws IOException {
        String moduleName = src.getName();
        TexturePacker.process(src.getAbsolutePath(), dst.getAbsolutePath(), moduleName);
        File srcJson = new File(src.getAbsolutePath() + "\\" + moduleName + ".json");
        //PrettyPrint to default
        if(srcJson.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            T[] objects = mapper.readValue(srcJson, runtimeType);
            mapper.writeValue(new File(dst.getAbsolutePath() + "\\" + moduleName + ".json"), objects);
        }
    }
}
