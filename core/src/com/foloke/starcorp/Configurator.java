package com.foloke.starcorp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Configurator {
    private static final String fileName = "config.json";
    public boolean vSync = false;
    public boolean fullscreen = false;
    public int monitorIndex = 0;
    public int displayModeIndex = -1;

    public int windowedHeight = 480;
    public int windowedWidth = 640;

    public Configurator() {

    }

    public static Configurator loadConfig() {
        Configurator configurator;
        FileHandle file = Gdx.files.local(fileName);
        if(file.exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                configurator = objectMapper.readValue(file.reader(), Configurator.class);
            } catch (Exception e) {
                e.printStackTrace();
                configurator = new Configurator();
            }
        } else {
            configurator = loadDefaultConfig();
        }

        return configurator;
    }

    private static Configurator loadDefaultConfig() {
        Configurator configurator = new Configurator();
        configurator.vSync = true;

        configurator.save();
        return new Configurator();
    }

    public void apply() {
        //DISPLAY
        Graphics.Monitor[] monitors = Gdx.graphics.getMonitors();
        if(fullscreen) {
            if (monitors.length > monitorIndex) {
                Graphics.Monitor monitor = monitors[monitorIndex];
                Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);

                if (displayModes.length < displayModeIndex || displayModeIndex == -1) {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode(monitor));
                } else {
                    Gdx.graphics.setFullscreenMode(displayModes[displayModeIndex]);
                }
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        } else {
            Gdx.graphics.setWindowedMode(windowedWidth, windowedHeight);
        }

        //VSYNC
        Gdx.graphics.setVSync(vSync);
    }

    public void save() {
        ObjectMapper objectMapper = new ObjectMapper();
        FileHandle file = Gdx.files.local(fileName);
        try {
            objectMapper.writeValue(file.writer(false), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Configurator cpy() {
        Configurator newConfig = new Configurator();
        newConfig.fullscreen = fullscreen;
        newConfig.displayModeIndex = displayModeIndex;
        newConfig.monitorIndex = monitorIndex;
        newConfig.vSync = vSync;
        newConfig.windowedHeight = windowedHeight;
        newConfig.windowedWidth = windowedWidth;

        return newConfig;
    }
}
