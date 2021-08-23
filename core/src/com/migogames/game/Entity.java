package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

import static com.migogames.game.Constants.PPM;

public class Entity {
    protected String name;
    protected float width, height;
    protected Body body;

    public static final int RUNNING = 0;
    public static final int IDLE = 1;
    public static final int JUMPING = 2;
    public static final int FALLING = 3;
    public static final int ATTACK1 = 4;
    public static final int ATTACK2 = 5;

    protected static final int FRAME_ROWS = 1;

    protected TextureRegion currentFrame;
    protected ArrayList<Animation> animations;
    protected int currentAnimation;
    protected float stateTime = 0;

    protected boolean runningRight;
    protected boolean canJump;
    protected boolean canAttack;
    protected int stepIndex = 0;

    protected World world;
    protected SpriteBatch batch;

    protected int healthPoints;
    protected int magicPoints;
    protected int currentHealthPoints;
    protected int currentMagicPoints;

    public Entity(String name, World world, SpriteBatch batch, float x, float y, int width, int height, Skin skin, int hp, int mp, int cHP, int cMP) {
        this.world = world;
        this.name = name;
        this.batch = batch;
        //Creating body
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef bodyFixture = new FixtureDef();
        bodyFixture.density = 1f;
        bodyFixture.friction = 0f;
        bodyFixture.shape = shape;

        body.createFixture(bodyFixture).setUserData(this);

        shape.dispose();

        this.width = width;
        this.height = height;
        this.runningRight = true;
        this.canJump = false;
        this.canAttack = false;
        this.healthPoints = hp;
        this.magicPoints = mp;
        this.currentHealthPoints = cHP;
        this.currentMagicPoints = cMP;

        animations = new ArrayList<Animation>();
    }

    public void setCurrentAnimation(int currentAnimation) {
        if (this.currentAnimation != currentAnimation) {
            this.currentAnimation = currentAnimation;
            stateTime = 0;
            stepIndex = 0;
        }
    }

    public void addTexture(TextureRegion textureRegion, int frames, float frameDuration) {
        TextureRegion[][] tmp = textureRegion.split(textureRegion.getRegionWidth() / frames,
                textureRegion.getRegionHeight() / FRAME_ROWS);
        TextureRegion[] textureFrames = new TextureRegion[frames * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < frames; j++) {
                textureFrames[index++] = tmp[i][j];
            }
        }
        Animation<TextureRegion> textureAnimation = new Animation(frameDuration, textureFrames);
        textureAnimation.setPlayMode(Animation.PlayMode.LOOP);

        animations.add(textureAnimation);
    }

    public void hitTheGround() {
        canJump = true;
    }

    public void update(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime() / 1.2f;
    }

    public int getCurrentHP() {
        return currentHealthPoints;
    }

    public int getCurrentMP() {
        return currentMagicPoints;
    }

    public int getMaxHP() {
        return healthPoints;
    }

    public int getMaxMP() {
        return magicPoints;
    }

    public float getX() {
        return body.getPosition().x * PPM;
    }

    public float getY() {
        return body.getPosition().y * PPM;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
