package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    public final Map<Integer, Integer> input;
    public final Map<Integer, Integer> output;
    public FactoryType factoryType;
    public float productionTime;

    public Recipe() {
        input = new HashMap<>();
        output = new HashMap<>();
    }

    private Recipe(Map<Integer, Integer> input, Map<Integer, Integer> output, FactoryType factoryType, float productionTime) {
        this.input = input;
        this.output = output;

        this.factoryType = factoryType;
        this.productionTime = productionTime;
    }
}
