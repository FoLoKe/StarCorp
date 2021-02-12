package com.foloke.starcorp.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.foloke.starcorp.AI.ShipAgent;
import com.foloke.starcorp.AI.SpaceLocation;
import com.foloke.starcorp.Sector;
import com.foloke.starcorp.StarCorpGame;
import com.foloke.starcorp.UI.GUI;
import com.foloke.starcorp.WorldController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerController {
    private Entity player;
    private final StarCorpGame core;
    public final int ACTIVATING_RADIUS = 1;
    private Sector currentSector;
    private boolean needsTransfer;

    ManualSteering manualSteering;

    public PlayerController(StarCorpGame core) {
        this.core = core;
    }

    private void handleInput(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            core.openInventory(player);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            core.getGui().openSettings();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            if(player instanceof Ship) {
                ShipAgent shipAgent = ((Ship)player).shipAgent;
                Random random = new Random();
                Arrive<Vector2> steeringBehavior = new Arrive<>(shipAgent);
                steeringBehavior.setArrivalTolerance(1);
                steeringBehavior.setDecelerationRadius(shipAgent.getMaxLinearSpeed() * shipAgent.forwardForce / shipAgent.breakingForce);
                steeringBehavior.setTarget(new SpaceLocation(new Vector2(0, 1000), 0));
                ((Ship) player).shipAgent.setSteeringBehavior(steeringBehavior);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            core.zoom(-1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            core.zoom(1);
        }

        if(player == null || player.destroyed) {
            return;
        }

        if(manualSteering != null) {

            if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                ((Ship) player).shipAgent.toggleDamping();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                manualSteering.forward();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                manualSteering.left();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                manualSteering.backward();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                manualSteering.right();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                manualSteering.strafeLeft();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                manualSteering.strafeRight();
            }
        }
    }

    public void act(float delta) {
        handleInput(delta);

        if(player != null) {
            if(player.active && player instanceof Ship) {
                GUI.debugLabel1.setText("velocity: " + ((Ship)player).getVelocity());
                ((Ship)player).shipAgent.update(delta);
            }
            if (currentSector == null || !currentSector.getCoordinates().equals(Sector.getSector(player))) {
                needsTransfer = true;
            }
        }
    }

    public void transfer(Sector newSector) {
        currentSector = newSector;
        needsTransfer = false;
    }

    public Vector2 getPlayerPosition() {
        return player.getPos();
    }

    public void setEntity(Entity entity) {
        this.player = entity;
        needsTransfer = true;

        if(entity instanceof Ship) {
            manualSteering = new ManualSteering(((Ship) entity).shipAgent);
            ((Ship)player).shipAgent.setSteeringBehavior(manualSteering);
        } else {
            manualSteering = null;
        }
    }

    public Sector getCurrentSector() {
        return currentSector;
    }

    public Entity getPlayer() {
        return player;
    }

    public boolean isNeedsTransfer() {
        return needsTransfer;
    }

    public int getX() {
        return currentSector.getCoordinates().x;
    }

    public int getY() {
        return currentSector.getCoordinates().y;
    }
    public List<Sector> getSensed(WorldController.SectorsContainer sectors) {
        return getSensed(sectors, new Sector.iVector2());
    }

    public List<Sector> getSensed(WorldController.SectorsContainer sectors, Sector.iVector2 offset) {
        List<Sector> sensed = new ArrayList<>();
        int minX = MathUtils.clamp(getX() - ACTIVATING_RADIUS + offset.x, 0, WorldController.WORLD_SIZE - 1);
        int maxX = MathUtils.clamp(getX() + ACTIVATING_RADIUS + offset.x, 0, WorldController.WORLD_SIZE - 1);

        int minY = MathUtils.clamp(getY() - ACTIVATING_RADIUS + offset.y, 0, WorldController.WORLD_SIZE - 1);
        int maxY = MathUtils.clamp(getY() + ACTIVATING_RADIUS + offset.y, 0, WorldController.WORLD_SIZE - 1);

        for(int i = minX; i <= maxX; i++) {
            for(int j = minY; j <= maxY; j++) {
                sensed.add(sectors.getSector(i, j));
            }
        }
        return sensed;
    }
}
