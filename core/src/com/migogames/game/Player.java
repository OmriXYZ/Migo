package com.migogames.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.migogames.game.Constants.PPM;

public class Player {
    private String name;
    private Body body;

    public Player(String name, World world, float x, float y, int width, int height) {
        this.name = name;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM / 2, height / PPM / 2);

        body.createFixture(shape, 1.0f);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public void update() {
        int xForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xForce += 1;
        }

        body.setLinearVelocity(xForce, body.getLinearVelocity().y);
    }
}
