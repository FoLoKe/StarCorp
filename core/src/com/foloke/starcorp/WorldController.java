package com.foloke.starcorp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.foloke.starcorp.Entities.Entity;
import com.foloke.starcorp.Entities.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class WorldController {

    public static final int WORLD_SIZE = 128;

    private final World world;
    private final StarCorpGame core;

    List<PlayerController> controllerList;

    SectorsContainer sectors;

    public WorldController(StarCorpGame core) {
        this.core = core;

        world = new World(new Vector2(0, 0), true);

//        for(int i =0; i < WORLD_SIZE; i++) {
//            for(int j = 0; j < WORLD_SIZE; j++) {
//                sectors[i][j] = new Sector(i, j, this);
//            }
//        }
        sectors = new SectorsContainer();

        controllerList = new ArrayList<>();
    }

    public void act(SpriteBatch batch, float delta) {
        sectors.act(batch, delta);

        for (PlayerController controller : controllerList) {
            controller.act(delta);

            if (controller.isNeedsTransfer()) {
                Sector.iVector2 newCoordinates = Sector.getSector(controller.getPlayer());

                if (controller.getCurrentSector() == null) {
                    controller.transfer(sectors.getSector(newCoordinates.x, newCoordinates.y));
                }

                List<Sector> toActivate = controller.getSensed(sectors, newCoordinates.sub(controller.getCurrentSector().getCoordinates()));
                List<Sector> active = controller.getSensed(sectors);

                active.removeAll(toActivate);
                for (Sector sector : active) {
                    sector.deactivate();
                }

                for (Sector sector : toActivate) {
                    sector.activate();
                }

                controller.transfer(sectors.getSector(newCoordinates.x, newCoordinates.y));
            }
        }
        world.step(1/60f, 16, 16);
    }

    public void debug(ShapeRenderer debugRenderer) {
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        sectors.debug(debugRenderer);
        debugRenderer.end();
    }


    public void addEntity(Entity entity) {
        transfer(entity, Sector.getSector(entity));
    }

    public void dispose() {
        world.dispose();
    }

    public World getWorld() {
        return world;
    }

    public boolean input(float x, float y) {
        Array<Fixture> fixtures = new Array<>();
        world.getFixtures(fixtures);
        Array.ArrayIterable<Fixture> iterable = new Array.ArrayIterable<>(fixtures);
        for (Fixture fixture : iterable) {
            if (fixture.testPoint(x, y)) {
                Object entity = fixture.getUserData();
                if(entity instanceof Entity) {
                    core.openInventory((Entity) entity);
                    return true;
                }
            }
        }
        return false;
    }

    public void transfer(Entity entity, Sector.iVector2 coordinates) {
        Sector sector = sectors.getSector(coordinates);
        sector.addEntity(entity);
        if(sector.isActive() && !entity.isActive()) {
            entity.activate(world);
        } else if (!sector.isActive() && entity.isActive()) {
            entity.deactivate(world);
        }
    }

    public void addController(PlayerController controller) {
        controllerList.add(controller);
    }

    public class SectorsContainer {
        private final Sector[][] sectors = new Sector[WORLD_SIZE][WORLD_SIZE];

        public Sector getSector(int x, int y) {
            x = MathUtils.clamp(x, 0, WORLD_SIZE - 1);
            y = MathUtils.clamp(y, 0, WORLD_SIZE - 1);

            if(sectors[x][y] == null) {
                sectors[x][y] = new Sector(x, y, WorldController.this);
            }

            return sectors[x][y];
        }

        public Sector getSector(Sector.iVector2 coordinates) {
            return getSector(coordinates.x, coordinates.y);
        }

        public void act(SpriteBatch batch, float delta) {
            for(int i =0; i < WORLD_SIZE; i++) {
                for(int j = 0; j < WORLD_SIZE; j++) {
                    if(sectors[i][j] != null) {
                        sectors[i][j].act(batch, delta);
                    }
                }
            }
        }

        public void debug(ShapeRenderer debugRenderer) {
            for(int i =0; i < WORLD_SIZE; i++) {
                for(int j = 0; j < WORLD_SIZE; j++) {
                    if(sectors[i][j] != null) {
                        sectors[i][j].debug(debugRenderer);
                    }
                }
            }
            debugRenderer.setColor(Color.BLUE);
            debugRenderer.box(0, 0, 0,
                    WORLD_SIZE * Sector.SECTOR_SIZE, WORLD_SIZE * Sector.SECTOR_SIZE, 0);
        }
    }
}
