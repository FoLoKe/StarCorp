package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.packer.ContainerType;
import com.foloke.starcorp.packer.PItem;

import java.util.Iterator;
import java.util.Map;

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
        return couldTake(item.getData(), count);

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

    public boolean couldTake(PItem itemData, int count) {
        return volume + itemData.volume * count <= maxVolume && containerType == itemData.type;
    }

    public boolean couldTake(Map<Integer, Integer> itemsMap) {
        int targetVolume = 0;
        for (Map.Entry<Integer, Integer> entry: itemsMap.entrySet()) {
            PItem itemData =  ItemsSheet.getItemData(entry.getKey());
            targetVolume += itemData.volume;
            if(volume + targetVolume > maxVolume || !couldTake(itemData, entry.getValue())) {
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
            if(inventoryItem.getId() == item.getId()) {
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

    public int removeItem(Item item) {
        return removeItem(item, item.getCount());
    }

    public int removeItem(Item item, int count) {
        return removeItem(item.getData(), count);
    }

    public int removeItem(PItem itemData, int count) {
        return removeItem(itemData.id, count);
    }

    public int removeItem(int id, int count) {
        for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
            Item item = it.next();
            if(item.getId() == id) {
                int result = count - item.getCount();
                if(result < 0) {
                    item.remove(count);
                    return 0;
                } else {
                    it.remove();
                }

                return result;
            }
        }

        return count;
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

    public void removeItems(Array<Item> items) {
        for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
            Item item = it.next();
            if(removeItem(item) == 0) {
                it.remove();
            }
        }
    }

    public void removeItems(Map<Integer, Integer> items) {
        for (Iterator<Map.Entry<Integer, Integer>> it = items.entrySet().iterator();
             it.hasNext(); ) {

            Map.Entry<Integer, Integer> entry = it.next();
            int result = removeItem(entry.getKey(), entry.getValue());
            if(result > 0) {
                entry.setValue(result);
            } else {
                it.remove();
            }
        }
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public float getVolume() {
        return volume;
    }
}
