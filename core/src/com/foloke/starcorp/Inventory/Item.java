package com.foloke.starcorp.Inventory;

public class Item {
    final int id;
    final private float volume;
    public ContainerType type;
    private int count;

    public Item(int id, float volume, int count, ContainerType type) {
        this.id = id;
        this.volume = volume;
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void add(int count) {
        this.count += count;
    }

    public void remove(int count) {
        this.count -= count;
    }

    public Item separate(int count) {
        if(count > 0) {
            Item separated = new Item(id, volume, count, type);
            this.count -= count;
            return separated;
        }

        return null;
    }

    public Item separateVolume(float volumeToSeparate) {
        if(volumeToSeparate < volume) {
            int count = (int) Math.floor(volumeToSeparate / volume);
            return separate(count);
        }

        return null;
    }

    public int getId() {
        return id;
    }

    public Item cpy() {
        return new Item(id, volume, count, type);
    }

    public float getVolume() {
        return volume * count;
    }

    public float getVolume(int count) {
        return volume * count;
    }
}
