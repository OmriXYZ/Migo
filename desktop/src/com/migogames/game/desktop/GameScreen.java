package com.migogames.game.desktop;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.migogames.game.Assets;
import com.migogames.game.Hud;
import com.migogames.game.MapBodyBuilder;
import com.migogames.game.Player;
import com.migogames.game.contactListenres.ContactListers;

import static com.migogames.game.Constants.FLOOR;
import static com.migogames.game.Constants.WALL;
import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private Skin skin;
    private World world;
    private Box2DDebugRenderer debugger;
    private Player player;

    private Hud hudPlayer;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMap tileMap;

    private Assets assets;

    private RayHandler rayHandler;
    private PointLight pointLight;

    private Array<Body> bodiesToDestroy;

    public GameScreen(OrthographicCamera orthographicCamera) {
        this.camera = orthographicCamera;
        camera.zoom = 1f;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,-9.8f), false);
        this.debugger = new Box2DDebugRenderer();
        this.world.setContactListener(new ContactListers());

        assets = new Assets();
        assets.load();
        assets.assetManager.finishLoading();

        skin = new Skin();
        skin.addRegions(assets.assetManager.get("skins.atlas", TextureAtlas.class));

        bodiesToDestroy = new Array<Body>();

        player = new Player("Itay", world, 250, 225, 58, 86, skin, 100, 250, 100, 250, bodiesToDestroy);

        tileMap = new TmxMapLoader().load("map/World.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        MapBodyBuilder.buildShapes(tileMap, PPM, world, WALL);
        MapBodyBuilder.buildShapes(tileMap, PPM, world, FLOOR);
        MapBodyBuilder.buildShapes(tileMap, PPM, world, "Particles");

        this.hudBatch = new SpriteBatch();
        hudPlayer = new Hud(hudBatch);

        rayHandler = new RayHandler(world);
        pointLight = new PointLight(rayHandler,500, Color.YELLOW,2000, player.getX()+100, player.getY()+500);

    }

    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        float x = camera.position.x - camera.viewportWidth * camera.zoom;
        float y = camera.position.y - camera.viewportHeight * camera.zoom;

        float width = camera.viewportWidth * camera.zoom * 2;
        float height = camera.viewportHeight * camera.zoom * 2;

        tiledMapRenderer.setView(camera.combined, x, y, width, height);
        tiledMapRenderer.render();

        //rayHandler.updateAndRender();
        //rayHandler.setCombinedMatrix(camera.combined);


        hudBatch.setProjectionMatrix(hudPlayer.getStage().getCamera().combined);
        hudPlayer.getStage().act(delta);
        hudPlayer.getStage().draw();

        debugger.render(world, camera.combined.scl(PPM));
        this.update();


    }

    private void update() {

        world.step(1/60f, 6, 2);

        if (!bodiesToDestroy.isEmpty()) {
            for (Body bodyToDestroy : bodiesToDestroy) {
                world.destroyBody(bodyToDestroy);
                bodiesToDestroy.removeValue(bodyToDestroy, true);
            }
        }

        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        player.update(batch);

        hudPlayer.updateHud(player.getMaxHP(), player.getMaxMP(), player.getMaxExpPoints(), player.getCurrentHP(), player.getCurrentMP(), player.getCurrentExpPoints(), hudBatch);


    }

    @Override
    public void dispose() {
        world.dispose();
        debugger.dispose();
        batch.dispose();
        skin.dispose();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        if (player.getBody().getPosition().x * PPM > 670)
            position.x = player.getBody().getPosition().x * PPM;
        else
            position.x = 670;

        if (player.getBody().getPosition().y * PPM > 250)
            position.y = player.getBody().getPosition().y * PPM + 475-250;
        else
            position.y = 475;
        if (player.getY() < 0) {
            player.setX(180);
            player.setY(300);
        }
       // position.y = player.getBody().getPosition().y * PPM + 200;

        camera.position.set(position);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1))
        camera.zoom += 0.05f;
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
            camera.zoom -= 0.05f;


        camera.update();
    }



}
