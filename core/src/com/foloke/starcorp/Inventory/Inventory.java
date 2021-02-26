package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;

public class Inventory {
    private final Array<Item> items;
    private float maxVolume;
    private float volume;
    private final ContainerType containerType;

    public Inventory(int maxVolume, ContainerType containerType) {
        items = new Array<>();
        this.maxVolume = maxVolume;
        volume = 0;
        this.containerType = containerType;
    }

    public boolean couldTake(Item item) {
        return couldTake(item, item.getCount());
    }

    public boolean couldTake(Item item, int count) {
        return volume + item.getVolume(count) <= maxVolume && containerType == item.type;
    }

    public boolean couldTake(Array<Item> items) {
        int targetVolume = 0;
        for (Item item: items.iterator()) {
            targetVolume += item.getVolume();
            if(volume + targetVolume > maxVolume || !couldTake(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean addItem(Item item) {
        float overCapacity = volume + item.getVolume() - maxVolume;
        if(overCapacity <= 0) {
            addSorted(item);
            return true;
        } else {
            Item separated = item.separateVolume(item.getVolume() - overCapacity);
            if(separated != null) {
                addSorted(separated);
            }
            return false;
        }
    }

    private void addSorted(Item item) {
        volume += item.getVolume();
        for (Item inventoryItem : items.iterator()) {
            if(inventoryItem.id == item.id) {
                inventoryItem.add(item.getCount());
                return;
            }
        }
        items.add(item);
    }

    public boolean addItems(Array<Item> items) {
        Array.ArrayIterator<Item> iterator = items.iterator();
        for (Item item : iterator) {
            if(!addItem(item)) {
                return false;
            } else {
                iterator.remove();
            }
        }

        return true;
    }

    public boolean removeItem(Item item) {
        return removeItem(item, item.getCount());
    }

    public boolean removeItem(Item item, int count) {
        if(items.contains(item, false)) {
            if(item.getCount() > count) {
                return false;
            } else if (item.getCount() == count) {
                items.removeValue(item, false);
                volume -= item.getVolume();
                return true;
            } else {
                item.remove(count);
                volume -= item.getVolume(count);
                return true;
            }
        }

        return false;
    }

    public boolean transfer(Item item, int count, Inventory inventory) {
        if(items.contains(item, false) && inventory.couldTake(item, count)) {
            if(item.getCount() < count) {
                inventory.addItem(item.separate(count));
                return true;
            } else if (item.getCount() == count) {
                removeItem(item);
                inventory.addItem(item);
            }
        }

        return false;
    }

    public boolean transfer(Item item, Inventory inventory) {
        return transfer(item, item.getCount(), inventory);
    }

    public boolean setMaxCapacity(float maxCapacity) {
        if(volume <= maxCapacity) {
            this.maxVolume = maxCapacity;
            return true;
        }
        return false;
    }

    public Array<Item> getItems() {
        return items;
    }

    public boolean removeItems(Array<Item> items) {
        for (Item item : items.iterator()) {
            if(!removeItem(item)) {
                return false;
            } else {
                items.removeValue(item, false);
            }
        }
        return true;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public float getVolume() {
        return volume;
    }
}
