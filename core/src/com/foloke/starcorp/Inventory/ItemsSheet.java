package com.foloke.starcorp.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foloke.starcorp.packer.PItem;
import com.foloke.starcorp.packer.PShip;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemsSheet {
    private static Map<Integer, PItem> items;

    private ItemsSheet() {

    }

    public static void init() {
        items = new HashMap<>();
        FileHandle coreItems = Gdx.files.internal("items/core/core.json");

        if(new File("items/core/core.atlas").exists()) {
            TextureAtlas textureAtlas = new TextureAtlas("items/core/core.atlas");
        }
        PItem[] itemsArray = new PItem[0];

        ObjectMapper mapper = new ObjectMapper();
        try {
            itemsArray = mapper.readValue(coreItems.reader(), PItem[].class);
        } catch (Exception e) {
            System.out.println(e);
        }

        for (PItem loadedItem: itemsArray) {
            items.put(loadedItem.id, loadedItem);
            //loadedItem.textureRegion = textureAtlas.findRegion(loadedItem.texture);
        }
    }

    public static PItem getItemData(int id) {
        return items.get(id);
    }

    public static Item create(int id) {
        return create(id, 1);
    }

    public static Item create(int id, int count) {
        if(items.containsKey(id)) {
            return create(items.get(id), count);
        } else {
            return new Item(-1, count);
        }
    }

    public static Item create(PItem itemData, int count) {
        return new Item(itemData, count);
    }

    public static Array<Item> create(Map<Integer, Integer> itemsMap) {
        Array<Item> items = new Array<>();
        for(Map.Entry<Integer, Integer> entry : itemsMap.entrySet()) {
            items.add(create(entry.getKey(), entry.getValue()));
        }

        return items;
    }
}
