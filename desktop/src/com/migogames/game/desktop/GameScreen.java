package com.migogames.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.migogames.game.Platform;
import com.migogames.game.Player;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer debugger;
    private Player player;
    private Platform platform;

    public GameScreen(OrthographicCamera orthographicCamera) {
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,-9.8f), false);
        this.debugger = new Box2DDebugRenderer();

        player = new Player("Itay", world, 10, 250, 32, 32);
        platform = new Platform(world, 10, 0, 500, 40);
    }

    public void render(float delta) {
        this.update();

        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        debugger.render(world, camera.combined.scl(PPM));
    }

    private void update() {
        world.step(1/60f, 6, 2);
        cameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        player.update();

    }

    @Override
    public void dispose() {
        world.dispose();
        debugger.dispose();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = player.getBody().getPosition().x * PPM;
        position.y = player.getBody().getPosition().y * PPM;
        camera.position.set(position);

        camera.update();
    }
}
