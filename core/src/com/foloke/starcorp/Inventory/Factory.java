package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

public class Factory implements Processor<Recipe>{

    private final Array<Recipe> queue;
    private Recipe currentRecipe;
    private float timeSpent;
    private boolean readyToProduce;
    private Inventory source;

    public Factory() {
        queue = new Array<>();
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

    @Override
    public void proceed(float delta) {
        if(readyToProduce) {
            if(timeSpent >= currentRecipe.productionTime && source.couldTake(currentRecipe.output)) {
                source.addItems(currentRecipe.output);
                readyToProduce = false;
            } else {
                timeSpent += delta;
            }
        } else if (queue.size > 0) {
            setRecipe(queue.first());
        }
    }

    private void setRecipe(Recipe newRecipe) {
        this.timeSpent = 0;

        if(currentRecipe == null) {
            this.currentRecipe = newRecipe.cpy();
        }

        acquireItems();

        if(readyToProduce) {
            queue.peek();
        }
    }

    private void acquireItems() {
        readyToProduce = source.removeItems(currentRecipe.input);
    }


}
