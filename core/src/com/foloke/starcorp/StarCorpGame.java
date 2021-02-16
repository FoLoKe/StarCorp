package com.foloke.starcorp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.*;
import com.foloke.starcorp.Entities.Entity;
import com.foloke.starcorp.Entities.PlayerController;
import com.foloke.starcorp.UI.GUI;

public class StarCorpGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;

	private GUI gui;

	private WorldController worldController;
	public static final float VH = 8f;

	//DEBUG
	private Box2DDebugRenderer dDebugRenderer;
	public ShapeRenderer debugRenderer;
	private PlayerController playerController;

	public static Configurator configurator;

	@Override
	public void create () {

		//RESEARCH STATED THAT BOX2D WORKS FINE ONLY WITH LOCKED STEPS, SO either we change static steps from
		//options (up to 240fps), which leads to computing costs, or live with 60fps locked to physics
		Gdx.graphics.setForegroundFPS(60);

		configurator = Configurator.loadConfig();
		configurator.apply();

		int WIDTH  = Gdx.graphics.getWidth();
		int HEIGHT = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		//WORLD
		worldController = new WorldController(this);

		//CAMs
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(VH * WIDTH / HEIGHT, VH, camera);

		gui = new GUI(new ScreenViewport());

		//DEBUG
		dDebugRenderer = new Box2DDebugRenderer();
		Entity entity = EntitiesFactory.getInstance().createEntity(1);
		worldController.addEntity(entity);
		MouseInputProcessor mouseInputProcessor = new MouseInputProcessor(this);
		playerController = new PlayerController(this);
		playerController.setEntity(entity);

		debugRenderer = new ShapeRenderer();

		//INPUT
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(gui);
		multiplexer.addProcessor(mouseInputProcessor);
		Gdx.input.setInputProcessor(multiplexer);

		worldController.addController(playerController);
	}

	@Override
	public void render () {


		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.apply();
		camera.position.set(playerController.getPlayerPosition(), 0);
		camera.update();


		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.act(batch, Gdx.graphics.getDeltaTime());
		batch.end();

		debugRenderer.setProjectionMatrix(camera.combined);
		worldController.debug(debugRenderer);

		dDebugRenderer.render(worldController.getWorld(), camera.combined);
		gui.render();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gui.dispose();

		worldController.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
		gui.getViewport().update(width, height);
		gui.resize();
	}

	public void openInventory(Entity entity) {
		gui.openInventory(entity);
	}

	public boolean worldInput(int screenX, int screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		return worldController.input(ray.origin.x, ray.origin.y);
	}

	public void zoom(float amount) {
		float current = camera.zoom;
		float minZoom = 0.25f;
		float maxZoom = 100;

		float a = current + amount / 10 * current;
		a = MathUtils.clamp(a, minZoom, maxZoom);

		camera.zoom = a;
	}


	public GUI getGui() {
		return gui;
	}
}
