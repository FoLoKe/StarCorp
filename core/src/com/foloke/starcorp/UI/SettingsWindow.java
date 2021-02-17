package com.foloke.starcorp.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.foloke.starcorp.Configurator;
import com.foloke.starcorp.StarCorpGame;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class SettingsWindow extends VisWindow {
    GUI gui;

    //Settings row
    VisCheckBox fullscreenModeBox;
    VisSelectBox<SM> monitorSelection;
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

    private void populate() {
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

    private void addOptionsListeners() {
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
                updateDisplayModes(monitorSelection.getSelected(), 0);
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

    private void addButtonsListeners() {
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

    private void setDefaults() {
        fullscreenModeBox.setChecked(localConfig.fullscreen);

        SM[] monitors = SM.convert(Gdx.graphics.getMonitors());
        if(monitors.length > 0) {
            SM currentMonitor;
            if (monitors.length > localConfig.monitorIndex) {
                currentMonitor = monitors[localConfig.monitorIndex];
            } else {
                currentMonitor = monitors[0];
            }

            monitorSelection.setItems(monitors);
            monitorSelection.setSelected(currentMonitor);

            updateDisplayModes(currentMonitor, localConfig.displayModeIndex);
        }

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

    private void updateDisplayModes(SM sMonitor, int selectionIndex) {
        Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(sMonitor.monitor);

        if(displayModes.length > 0) {
            Graphics.DisplayMode currentDisplayMode;
            if(displayModes.length > selectionIndex) {
                currentDisplayMode = displayModes[selectionIndex];
            } else {
                currentDisplayMode = displayModes[0];
            }

            displayModeSelection.setItems(displayModes);
            displayModeSelection.setSelected(currentDisplayMode);
        }
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

    static class SM {
        int index;
        Graphics.Monitor monitor;

        public SM(Graphics.Monitor monitor, int index) {
            this.monitor = monitor;
            this.index = index;
        }

        @Override
        public String toString() {
            return index + ": " + monitor.name;
        }

        static SM[] convert(Graphics.Monitor[] monitors) {
            SM[] sMonitors = new SM[monitors.length];
            for (int i = 0; i < monitors.length; i++) {
                sMonitors[i] = new SM(monitors[i], i);
            }
            return sMonitors;
        }
    }
}
