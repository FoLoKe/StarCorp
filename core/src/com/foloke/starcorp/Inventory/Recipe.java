package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

import java.util.Map;

public class Recipe {
    public Array<Item> input;
    public Array<Item> output;
    public FactoryType factoryType;
    public float productionTime;

    public Recipe() {
        input = new Array<>();
        output = new Array<>();
    }

    private Recipe(Array<Item> input, Array<Item> output, FactoryType factoryType, float productionTime) {
        this();
        for (Item item : input.iterator()) {
            this.input.add(item.cpy());
        }

        for(Item item : output.iterator()) {
            this.output.add(item.cpy());
        }

        this.factoryType = factoryType;
        this.productionTime = productionTime;
    }

    public Recipe cpy() {
        return new Recipe(input, output, factoryType, productionTime);
    }
}
