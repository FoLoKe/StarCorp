package com.foloke.starcorp.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.Configurator;
import com.foloke.starcorp.StarCorpGame;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class SettingsWindow extends VisWindow {
    GUI gui;

    //Settings row
    VisCheckBox fullscreenModeBox;
    VisSelectBox<SMonitor> monitorSelection;
    VisSelectBox<Graphics.DisplayMode> displayModeSelection;
    VisTextField screenWidthField;
    VisTextField screenHeightField;
    VisCheckBox vSyncBox;

    //Buttons row
    VisTextButton cancelButton;
    VisTextButton revertButton;
    VisTextButton acceptButton;
    VisTextButton confirmButton;

    Configurator localConfig;

    public SettingsWindow(GUI gui) {
        super("Settings window");
        this.gui = gui;

        localConfig = StarCorpGame.configurator.cpy();

        fullscreenModeBox = new VisCheckBox("");
        monitorSelection = new VisSelectBox<>();
        displayModeSelection = new VisSelectBox<>();
        screenWidthField = new VisTextField();
        screenHeightField = new VisTextField();
        vSyncBox = new VisCheckBox("");

        cancelButton = new VisTextButton("cancel");
        revertButton = new VisTextButton("revert");
        acceptButton = new VisTextButton("accept");
        confirmButton = new VisTextButton("confirm");

        populate();
        setDefaults();
        addOptionsListeners();
        addButtonsListeners();

        updateOptionsVisibility();
        resize();
    }

    public void populate() {
        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setCenterOnAdd(true);
        setMovable(false);
        setResizable(false);

        setTouchable(Touchable.enabled);
        addCloseButton();

        VisTable table = new VisTable();
        table.align(Align.top);

        table.add(new VisLabel("Fullscreen: "));
        table.add(fullscreenModeBox).expandX().row();
        table.add(new VisLabel("Monitor: "));
        table.add(monitorSelection).expandX().fillX().row();
        table.add(new VisLabel("Resolution: "));
        table.add(displayModeSelection).expandX().fillX().row();
        table.add(new VisLabel("Windowed width: "));
        table.add(screenWidthField).expandX().fillX().row();
        table.add(new VisLabel("Windowed height: "));
        table.add(screenHeightField).expandX().fillX().row();
        table.add(new VisLabel("Vsync: "));
        table.add(vSyncBox).expandX().row();

        VisScrollPane scrollPane = new VisScrollPane(table);
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        add(scrollPane).expandX().fillX().expandY().fillY().row();

        VisTable buttonsTable = new VisTable();
        buttonsTable.add(cancelButton).expandX();
        buttonsTable.add(revertButton).expandX();
        buttonsTable.add(acceptButton).expandX();
        buttonsTable.add(confirmButton).expandX();

        add(buttonsTable).expandX().fillX().row();
        align(Align.top);
    }

    public void addOptionsListeners() {
        fullscreenModeBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                localConfig.fullscreen = fullscreenModeBox.isChecked();
                updateOptionsVisibility();
            }
        });

        monitorSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                localConfig.monitorIndex = monitorSelection.getSelectedIndex();
            }
        });

        displayModeSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                localConfig.displayModeIndex = displayModeSelection.getSelectedIndex();
            }
        });

        screenWidthField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String input = screenWidthField.getText();
                if(input.length() > 0) {
                    int inputInt = Integer.parseInt(input);
                    if(inputInt >= 640 && inputInt <= 4096) {
                        screenWidthField.setInputValid(true);
                        localConfig.windowedWidth = inputInt;
                    } else {
                        screenWidthField.setInputValid(false);
                    }
                } else {
                    screenWidthField.setInputValid(false);
                }
            }
        });

        screenHeightField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String input = screenHeightField.getText();
                if(input.length() > 0) {
                    int inputInt = Integer.parseInt(input);
                    if(inputInt >= 480 && inputInt <= 4096) {
                        screenHeightField.setInputValid(true);
                        localConfig.windowedHeight = inputInt;
                    } else {
                        screenHeightField.setInputValid(false);
                    }
                } else {
                    screenHeightField.setInputValid(false);
                }
            }
        });

        vSyncBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                localConfig.vSync = vSyncBox.isChecked();
            }
        });
    }

    public void addButtonsListeners() {
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                StarCorpGame.configurator.apply();
                close();
                remove();
            }
        });

        revertButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                StarCorpGame.configurator.apply();
            }
        });

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                localConfig.apply();
            }
        });

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                localConfig.save();
                StarCorpGame.configurator = localConfig;
                close();
                remove();
            }
        });
    }

    public void setDefaults() {
        fullscreenModeBox.setChecked(localConfig.fullscreen);

        Array<SMonitor> monitors = SMonitor.getSMonitors(Gdx.graphics.getMonitors());
        SMonitor currentMonitor = new SMonitor(0, Gdx.graphics.getMonitor());

        Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(currentMonitor.monitor);
        Graphics.DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();

        monitorSelection.setItems(monitors);
        displayModeSelection.setItems(displayModes);

        monitorSelection.setSelected(currentMonitor);
        displayModeSelection.setSelected(currentDisplayMode);

        screenWidthField.setTextFieldFilter(new IntDigitsOnlyFilter(false));
        screenWidthField.setText(localConfig.windowedWidth + "");
        screenHeightField.setTextFieldFilter(new IntDigitsOnlyFilter(false));
        screenHeightField.setText(localConfig.windowedHeight + "");

        vSyncBox.setChecked(localConfig.vSync);
    }

    private void updateOptionsVisibility() {
        monitorSelection.setDisabled(!localConfig.fullscreen);
        displayModeSelection.setDisabled(!localConfig.fullscreen);

        screenWidthField.setDisabled(localConfig.fullscreen);
        screenHeightField.setDisabled(localConfig.fullscreen);
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
