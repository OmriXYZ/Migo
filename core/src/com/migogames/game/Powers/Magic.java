package com.migogames.game.Powers;

import com.badlogic.gdx.physics.box2d.*;

import static com.migogames.game.Constants.PPM;

public class Magic {
    public Body body;
    public World world;
    public float destroyTimer = 0.07f;
    public boolean toBeDestroyed = false;

    public Magic(World world, float x, float y, float radius, boolean isRight) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        body = world.createBody(def);
        this.world = world;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / 2 / PPM);

        FixtureDef bodyFixture = new FixtureDef();
        bodyFixture.density = 0.0001f;
        bodyFixture.friction = 0f;
        bodyFixture.shape = shape;
        body.createFixture(bodyFixture).setUserData("MAGIC");
        body.setUserData("MAGIC");
        if (isRight) {
            body.setTransform(x / PPM + 2f, y / PPM, 0);
        } else {
            body.setTransform(x / PPM - 2f, y / PPM, 0);
        }
        shape.dispose();
    }

    public void update(float delta){
        destroyTimer-= delta;

        if(destroyTimer <= 0){
            toBeDestroyed = true;
        }

    }
}
