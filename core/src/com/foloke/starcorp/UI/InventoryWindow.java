package com.foloke.starcorp.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.foloke.starcorp.Entities.Entity;
import com.foloke.starcorp.Inventory.Item;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.util.Objects;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchUp;

public class InventoryWindow extends VisWindow {
    private final Entity entity;
    private GUI gui;

    public InventoryWindow(Entity entity, GUI gui) {
        super("inventory " + entity);
        this.entity = entity;
        this.gui = gui;

        setCenterOnAdd(false);
        Skin skin = VisUI.getSkin();
        columnDefaults(0).left();
        setResizable(true);
        setTouchable(Touchable.enabled);
        addCloseButton();
        VisTable table = new VisTable();
        table.align(Align.top);

        setDebug(true, true);
        //setResizeBorder(8);
        //pad(8);


        for(Item item : entity.getInventory().getItems()) {
            table.add(new VisLabel("item " + item.getId())).expandX().fillX().row();
        }

        ScrollPane scrollPane = new ScrollPane(table, skin, "list");
        //scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);

        add(scrollPane).expandX().fillX();
        align(Align.top);

        setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() * 4 / 5f);
        setPosition(Gdx.graphics.getWidth() / 12f, Gdx.graphics.getHeight() / 10f);

        addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    @Override
    protected void close() {
        super.close();
        gui.closeInventory(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryWindow window = (InventoryWindow) o;
        return entity == window.entity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void toFront() {
        super.toFront();
        gui.focused(this);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if(getWidth() < 100) {
            setWidth(100);
        }
        if(getHeight() < 100) {
            setHeight(100);
        }
    }
}
