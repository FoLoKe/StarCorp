package com.foloke.starcorp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.foloke.starcorp.Entities.Entity;

import java.util.ArrayList;
import java.util.Objects;

public class Sector {
    private final ArrayList<Entity> entities;
    private final ArrayList<Entity> toRemove;
    private final ArrayList<Entity> toAdd;

    private final WorldController controller;
    private final iVector2 coordinates;
    public static final int SECTOR_SIZE = 16;

    private boolean active = false;

    public Sector(int x, int y, WorldController controller) {
        this.coordinates = new iVector2(x, y);
        this.controller = controller;

        entities = new ArrayList<>();
        toRemove = new ArrayList<>();
        toAdd = new ArrayList<>();

        //DEBUG
        //Entity debugEntity = EntitiesFactory.createEntity(1);
        //debugEntity.setPosition(x * SECTOR_SIZE + SECTOR_SIZE / 2f, y * SECTOR_SIZE + SECTOR_SIZE / 2f);
        //toAdd.add(debugEntity);
    }

    public void act(SpriteBatch batch, float delta) {

        for (Entity entity : toRemove) {
            entity.dispose();
            entities.remove(entity);
        }
        toRemove.clear();

        if (toAdd.size() > 0) {
            entities.addAll(toAdd);
            toAdd.clear();
        }

        for (Entity entity : entities) {
            entity.tick(delta);

            if (entity.destroyed) {
                toRemove.add(entity);
            } else {
                iVector2 entityCoordinates = getSector(entity);
                if (!coordinates.equals(entityCoordinates)) {
                    toRemove.add(entity);
                    controller.transfer(entity, entityCoordinates);

                }
            }
        }

        if(active) {
            for (Entity entity : entities) {
                entity.render(batch);
            }
        }
    }

    public void debug(ShapeRenderer debugRenderer) {
        if(active) {
            debugRenderer.setColor(Color.GREEN);
        } else {
            debugRenderer.setColor(Color.RED);
        }
        debugRenderer.box(coordinates.x * SECTOR_SIZE, coordinates.y * SECTOR_SIZE, 0, SECTOR_SIZE, SECTOR_SIZE, 0);

    }

    public static iVector2 getSector(Entity entity) {
        int newX = 0;
        int newY = 0;
        if(entity != null) {
            Vector2 pos = entity.getPos();
            newX = (int) MathUtils.clamp(pos.x / Sector.SECTOR_SIZE, 0, WorldController.WORLD_SIZE - 1);
            newY = (int)MathUtils.clamp(pos.y / Sector.SECTOR_SIZE, 0, WorldController.WORLD_SIZE - 1);
        }
        return new iVector2(newX, newY);
    }

    public void addEntity(Entity entity) {
        toAdd.add(entity);
        if(this.isActive()) {
            entity.activate(controller.getWorld());
        } else {
            entity.deactivate(controller.getWorld());
        }
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        if(!this.isActive()) {
            for (Entity entity : entities) {
                if (!entity.destroyed) {
                    entity.activate(controller.getWorld());
                }
            }
            for (Entity entity : toAdd) {
                if (!entity.destroyed) {
                    entity.activate(controller.getWorld());
                }
            }
            this.active = true;
        }
    }

    public void deactivate() {
        if(this.isActive()) {
            for (Entity entity : entities) {
                if (!entity.destroyed) {
                    entity.deactivate(controller.getWorld());
                }
            }
            for (Entity entity : toAdd) {
                if (!entity.destroyed) {
                    entity.deactivate(controller.getWorld());
                }
            }
            this.active = false;
        }
    }

    public iVector2 getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sector sector = (Sector) o;
        return coordinates.equals(sector.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    public static class iVector2 {
        public int x, y;

        public iVector2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public iVector2() {

        };

        public iVector2 sub(iVector2 iVector2) {
            return new iVector2(x - iVector2.x, y - iVector2.y);
        }

        public iVector2 unsigned() {
            return new iVector2(x < 0 ? -x:x, y < 0 ? -y:y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            iVector2 vector2 = (iVector2) o;
            return x == vector2.x && y == vector2.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
