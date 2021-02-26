package com.foloke.starcorp.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.foloke.starcorp.Inventory.ContainerType;
import com.foloke.starcorp.Inventory.Inventory;
import com.foloke.starcorp.Inventory.Item;
import com.foloke.starcorp.packer.PEntity;

public class Entity {
    protected Body body;

    public boolean destroyed;
    private final Sprite sprite;

    protected boolean active;
    private final Transform simTransform;
    private final Vector2[] collisionVertices;

    //DEBUG
    Inventory inventory;


    public Entity(PEntity pEntity) {
        //createBody(vertices, world);
        this.collisionVertices = pEntity.vertices;

        simTransform = new Transform();

        this.sprite = new Sprite(pEntity.atlasRegion);
        sprite.setSize(1, 1);
        sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);

        //DEBUG:
        inventory = new Inventory(3, ContainerType.SOLID);
        for(int i = 0; i < 20; i++) {
            inventory.addItem(new Item(i, 15, 10, ContainerType.SOLID));
        }
    }

    public void tick(float delta) {
        if(active) {
            simTransform.setPosition(body.getPosition());
            simTransform.setRotation(body.getAngle());
        }
        sprite.setOriginBasedPosition(simTransform.getPosition().x, simTransform.getPosition().y);
        sprite.setRotation(simTransform.getRotation() * MathUtils.radiansToDegrees);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    protected void createBody(Vector2[] vertices, World world) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);

        body = world.createBody(def);
        body.setAngularDamping(1f);
        body.setLinearDamping(0.1f);
        body.setTransform(simTransform.getPosition(), simTransform.getRotation());

        Fixture collision = body.createFixture(polygonShape, 1);
        collision.setUserData(this);

        polygonShape.dispose();
    }

    public void activate(World world) {
        if(body == null) {
            createBody(collisionVertices, world);
        }
        active = true;
    }

    public void deactivate(World world) {
        active = false;

        if(body != null) {
            world.destroyBody(body);
            body = null;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void dispose() {

    }

    public void setPosition(float x, float y) {
        if(active) {
            body.setTransform(x, y, body.getAngle());
        }
        simTransform.setPosition(new Vector2(x, y));
    }

    public void setPosition(Vector2 position) {
        setPosition(position.x, position.y);
    }

    public void setRotationDeg(float angle) {
        if(active) {
            body.setTransform(body.getPosition(), angle * MathUtils.degreesToRadians);
        }
        simTransform.setRotation(angle * MathUtils.degreesToRadians);
    }

    public void setRotationRad(float angle) {
        if(active) {
            body.setTransform(body.getPosition(), angle);
        }
        simTransform.setRotation(angle);
    }

    public void forceStop() {
        body.setLinearVelocity(0, 0);
    }

    public Vector2 getPos() {
        return simTransform.getPosition();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
