package com.foloke.starcorp.Inventory;

import com.foloke.starcorp.packer.ContainerType;
import com.foloke.starcorp.packer.PItem;

public class Item {
    private final PItem itemData;
    private int count;

    public Item(int id, int count) {
        this(ItemsSheet.getItemData(id), count);
    }

    public Item(PItem itemData, int count) {
        this.itemData = itemData;
        this.count = count;
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
            Item separated = ItemsSheet.create(itemData, count);
            this.count -= count;
            return separated;
        }

        return null;
    }

    public Item separateVolume(float volumeToSeparate) {
        if(volumeToSeparate < itemData.volume) {
            int count = (int) Math.floor(volumeToSeparate / itemData.volume);
            return separate(count);
        }

        return null;
    }

    public int getId() {
        return itemData.id;
    }

    public Item cpy() {
        return new Item(itemData, count);
    }

    public float getVolume() {
        return itemData.volume * this.count;
    }

    public float getVolume(int count) {
        return itemData.volume * count;
    }

    public PItem getData() {
        return itemData;
    }
}
