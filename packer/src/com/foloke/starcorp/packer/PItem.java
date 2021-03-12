package com.foloke.starcorp.packer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PItem {
    public int id;
    public float volume;
    public ContainerType type;
    public String texture;

    //JSON ignore
    @JsonIgnore
    public TextureRegion textureRegion;

    public PItem() {

    }

    public PItem(int id, float volume, ContainerType type) {
        this.id = id;
        this.volume = volume;
        this.type = type;
    }
}
