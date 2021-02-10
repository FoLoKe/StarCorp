package com.foloke.starcorp.Inventory;

public class Item {
    final int id;
    private int count;

    public Item(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void add(int count) {
        count += count;
    }

    public int remove(int count) {
        this.count -= count;
        return count;
    }

    public Item separate(int count) {
        Item separated = new Item(id, count);
        this.count -= count;
        return separated;
    }

    public int getId() {
        return id;
    }
}
