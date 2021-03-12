package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class Factory implements Processor<Recipe> {

    private final Array<Recipe> queue;
    private Recipe currentRecipe;
    private float timeSpent;
    private Inventory source;

    private Array<Item> output;
    private final Map<Integer, Integer> needToAcquire;

    public Factory() {
        queue = new Array<>();
        needToAcquire = new HashMap<>();
    }

    @Override
    public void add(Recipe request) {
        queue.add(request);
    }

    @Override
    public void remove(Recipe request) {
        queue.removeValue(request, false);
    }

    @Override
    public void remove(int index) {
        queue.removeIndex(index);
    }

    enum ProductionState {
        WAITING, ACQUIRING, PRODUCING, OUTPUTTING
    }

    ProductionState state = ProductionState.WAITING;

    @Override
    public void proceed(float delta) {
        switch (state) {
            case WAITING:
                if (queue.size > 0) {
                    state = ProductionState.ACQUIRING;
                }
                break;

            case ACQUIRING:
                setRecipe(queue.first());
                break;

            case PRODUCING:
                if (timeSpent >= currentRecipe.productionTime) {
                    state = ProductionState.OUTPUTTING;
                } else {
                    timeSpent += delta;
                }
                break;

            case OUTPUTTING:
                if (output == null) {
                    ItemsSheet.create(currentRecipe.output);
                }

                if (output.size > 0) {
                    if (source.addItems(output)) {
                        output = null;
                    }
                } else {
                    state = ProductionState.WAITING;
                }
                break;
        }
    }


    private void setRecipe(Recipe newRecipe) {
        if(currentRecipe == null) {
            this.timeSpent = 0;
            this.currentRecipe = newRecipe;
            for (Map.Entry<Integer, Integer> entry : currentRecipe.input.entrySet()) {
                needToAcquire.put(entry.getKey(), entry.getKey());
            }
        }

        source.removeItems(needToAcquire);

        if(needToAcquire.size() == 0) {
            queue.peek();
            state = ProductionState.PRODUCING;
        }
    }
}
