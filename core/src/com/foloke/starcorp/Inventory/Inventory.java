package com.foloke.starcorp.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Item> items;
    private int maxCapacity;
    private int capacity;

    public Inventory(int maxCapacity) {
        items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        capacity = 0;
    }

    public boolean couldTake(Item item) {
        return capacity + item.getCount() <= maxCapacity;
    }

    public boolean couldTake(Item item, int count) {
        return capacity + count <= maxCapacity;
    }

    public boolean addItem(Item item) {
        int overCapacity = capacity + item.getCount() - maxCapacity;
        if(overCapacity <= 0) {
            capacity += item.getCount();
            items.add(item);
            return true;
        } else {
            Item separated = item.separate(overCapacity);
            capacity += separated.getCount();
            items.add(separated);
            return false;
        }
    }

    public boolean removeItem(Item item) {
        return removeItem(item, item.getCount());
    }

    public boolean removeItem(Item item, int count) {
        if(items.contains(item)) {
            if(item.getCount() > count) {
                return false;
            } else if (item.getCount() == count) {
                items.remove(item);
                capacity -= item.getCount();
                return true;
            } else {
                item.remove(count);
                capacity -= count;
                return true;
            }
        }

        return false;
    }

    public boolean transfer(Item item, int count, Inventory inventory) {
        if(items.contains(item)) {
            if(item.getCount() <= count && inventory.couldTake(item, count)) {
                removeItem(item, count);
                inventory.addItem(new Item(item.id, count));
                return true;
            }
        }

        return false;
    }

    public boolean transfer(Item item, Inventory inventory) {
        return transfer(item, item.getCount(), inventory);
    }

    public boolean setMaxCapacity(int maxCapacity) {
        if(capacity <= maxCapacity) {
            this.maxCapacity = maxCapacity;
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }
}
