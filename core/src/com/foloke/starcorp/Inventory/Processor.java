package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

public interface Processor<T> {

    void add(T item);
    void remove(T item);
    void remove(int index);

    void proceed(float delta);

}
