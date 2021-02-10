package com.foloke.starcorp;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MouseInputProcessor implements InputProcessor {
    StarCorpGame core;

    public MouseInputProcessor(StarCorpGame core) {
        this.core = core;
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.ESCAPE) {
            System.exit(0);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return core.worldInput(screenX, screenY);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        core.zoom(amountY);
        return true;
    }
}
