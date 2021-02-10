package com.foloke.starcorp.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.foloke.starcorp.Entities.Entity;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class GUI extends Stage {
    Array<InventoryWindow> inventoryWindows;
    VisWindow focusedWindow;
    public static Label debugLabel;
    public static Label debugLabel1;

    public GUI(ScreenViewport screenViewport) {
        super(screenViewport);

        VisUI.load(VisUI.SkinScale.X1);
        VisTable table = new VisTable();
        table.pad(10f);
        table.setFillParent(true);
        table.align(Align.left | Align.top);
        debugLabel = new VisLabel("hello world!");
        table.add(debugLabel).row();
        debugLabel1 = new VisLabel("hello world!");
        table.add(debugLabel1);
        addActor(table);

        inventoryWindows = new Array<>();
        inventoryWindows.ordered = true;
    }

    public void render() {
        getViewport().apply(true);

        debugLabel.setText(Gdx.graphics.getFramesPerSecond());
        act(Gdx.graphics.getDeltaTime());
        draw();
    }

    public void openInventory(Entity entity) {
        InventoryWindow window = new InventoryWindow(entity, this);
        if(inventoryWindows.contains(window, false)) {
            Array.ArrayIterable<InventoryWindow> iterator = new Array.ArrayIterable<>(inventoryWindows);
            for (InventoryWindow inventoryWindow : iterator) {
                if(entity.equals(inventoryWindow.getEntity())) {
                    inventoryWindow.toFront();
                    focusedWindow = inventoryWindow;
                    return;
                }
            }
        } else {
            addActor(window);
            inventoryWindows.add(window);
            if(focusedWindow instanceof InventoryWindow) {
                window.setPosition(focusedWindow.getX() + 16, focusedWindow.getY() - 16);
            }
            if (inventoryWindows.size > 3) {
                inventoryWindows.first().close();
            }
            focusedWindow = window;
        }
    }

    public void closeInventory(InventoryWindow window) {
        inventoryWindows.removeValue(window, true);
        if(inventoryWindows.size > 0) {
            focusedWindow = inventoryWindows.get(inventoryWindows.size - 1);
        } else {
            focusedWindow = null;
        }
    }

    public void focused(VisWindow window) {
        focusedWindow = window;
    }
}
