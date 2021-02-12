package com.foloke.starcorp.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.Configurator;
import com.foloke.starcorp.StarCorpGame;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class SettingsWindow extends VisWindow {
    GUI gui;
    public SettingsWindow(GUI gui) {
        super("Settings window");

        this.gui = gui;
        final Configurator configurator = new Configurator();
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setCenterOnAdd(true);
        setMovable(false);
        setResizable(false);

        setTouchable(Touchable.enabled);
        addCloseButton();

        VisTable table = new VisTable();
        table.align(Align.top);

        final VisCheckBox fullscreenModeBox = new VisCheckBox("");
        boolean fullscreenState = StarCorpGame.configurator.fullscreen;
        fullscreenModeBox.setChecked(fullscreenState);
        fullscreenModeBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configurator.fullscreen = fullscreenModeBox.isChecked();
            }
        });
        table.add(new VisLabel("Fullscreen"));
        table.add(fullscreenModeBox).expandX().row();

        Array<SMonitor> monitors = SMonitor.getSMonitors(Gdx.graphics.getMonitors());
        SMonitor currentMonitor = new SMonitor(0, Gdx.graphics.getMonitor());

        Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(currentMonitor.monitor);
        Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();

        final VisSelectBox<SMonitor> monitorSelection = new VisSelectBox<>();
        final VisSelectBox<Graphics.DisplayMode> displayModeSelection = new VisSelectBox<>();

        monitorSelection.setItems(monitors);
        displayModeSelection.setItems(displayModes);

        debugAll();

        monitorSelection.setSelected(currentMonitor);
        monitorSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configurator.monitorIndex = monitorSelection.getSelectedIndex();
            }
        });

        displayModeSelection.setSelected(currentDisplayMode);
        displayModeSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                configurator.displayModeIndex = displayModeSelection.getSelectedIndex();
            }
        });

        table.add(new VisLabel("Monitor: "));
        table.add(monitorSelection).expandX().fillX().row();
        table.add(new VisLabel("Resolution: "));
        table.add(displayModeSelection).expandX().fillX().row();

        VisScrollPane scrollPane = new VisScrollPane(table);
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        add(scrollPane).expandX().fillX().expandY().fillY().row();

        VisTable buttonsTable = new VisTable();

        VisTextButton cancelButton = new VisTextButton("cancel");
        buttonsTable.add(cancelButton).expandX();
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                StarCorpGame.configurator.apply();
                close();
                remove();
            }
        });

        VisTextButton revertButton = new VisTextButton("revert");
        buttonsTable.add(revertButton).expandX();
        revertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                StarCorpGame.configurator.apply();
            }
        });

        VisTextButton acceptButton = new VisTextButton("accept");
        buttonsTable.add(acceptButton).expandX();
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                configurator.apply();
            }
        });



        VisTextButton confirmButton = new VisTextButton("confirm");
        buttonsTable.add(confirmButton).expandX();
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                configurator.save();
                StarCorpGame.configurator = configurator;
                close();
                remove();
            }
        });

        add(buttonsTable).expandX().fillX().row();

        align(Align.top);
        resize();
    }

    public void resize() {
        setSize(Gdx.graphics.getWidth() * 0.8f, Gdx.graphics.getHeight() * 0.8f);
        setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.1f);
    }

    @Override
    protected void close() {
        super.close();
        gui.closeSettings();
    }

    private static class SMonitor {
        int index;
        Graphics.Monitor monitor;

        public SMonitor(int index,  Graphics.Monitor monitor) {
            this.index = index;
            this.monitor = monitor;
        }

        @Override
        public String toString() {
            return index + ": " + monitor.name;
        }

        public static Array<SMonitor> getSMonitors(Graphics.Monitor[] monitors) {
            Array<SMonitor> sMonitors = new Array<>();
            for (int i = 0; i < monitors.length; i++) {
                sMonitors.add(new SMonitor(i, monitors[i]));
            }

            return sMonitors;
        }
    }
}
