package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.migogames.game.Powers.Magic;
import com.migogames.game.Powers.PowerBall;

import static com.migogames.game.Constants.PPM;

public class Player extends Entity {

    private boolean throwPowerBall, attack1;
    private Array<Body> tmpBodies = new Array<Body>();
    private Image imgPowerBall;

    private final int POWERBALL = 10;
    private final int MAGIC = 5;

    public Array<PowerBall> powerBalls = new Array<PowerBall>();
    public Array<Magic> magicks = new Array<Magic>();

    public Array<Body> bodiesToDestroy;

    private Timer timer;
    private Timer.Task manaTask;

    private int expPoints;
    private int currentExpPoints;
    private int level;


    public Player(String name, World world, SpriteBatch batch, float x, float y, int width, int height, Skin skin, int hp, int mp, int cHP, int cMP, Array<Body> bodiesToDestroy) {
        super(name, world, batch, x, y, width, height, skin, hp, mp, cHP, cMP);

        this.expPoints = 100;
        this.currentExpPoints = 0;

        this.bodiesToDestroy = bodiesToDestroy;

        addTexture(skin.getRegion("run"), 8, 0.065f);
        addTexture(skin.getRegion("idle"), 6, 0.07f);
        addTexture(skin.getRegion("jump"), 2, 0.05f);
        addTexture(skin.getRegion("fall"), 2, 0.05f);
        addTexture(skin.getRegion("attack1"), 8, 0.04f);
        addTexture(skin.getRegion("attack2"), 8, 0.04f);

        setCurrentAnimation(IDLE);

        throwPowerBall = false;
        attack1 = false;
        Texture txPowerBall = new Texture("player/powerball.png");
        imgPowerBall = new Image(txPowerBall);

        timer = new Timer();
        manaTask = new Timer.Task() {
            @Override
            public void run() {
                if (currentMagicPoints < magicPoints)
                    currentMagicPoints++;
                timer.scheduleTask(manaTask, 0.5f);
            }
        };
        timer.scheduleTask(manaTask, 0.5f);

        body.setUserData("PLAYER");

    }

    public Body getBody() {
        return body;
    }

    public void update(SpriteBatch batch) {
        super.update(batch);
        updateMovementTexture(batch);

    }


    public void updateMovementTexture(SpriteBatch batch) {
        currentFrame = (TextureRegion) animations.get(currentAnimation).getKeyFrame(stateTime, true);

        int xForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (canAttack)
                xForce -= 1f;
            else
                xForce -= 2f;
            runningRight = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (canAttack)
                xForce += 1f;
            else
                xForce += 2f;
            runningRight = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (canJump) {
                body.applyForceToCenter(0, 3000, false);
                canJump = false;
            }
        }


        batch.begin();
        if (runningRight)
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2), body.getPosition().y * PPM - (height / 2), currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        else if (currentAnimation == ATTACK1 || currentAnimation == ATTACK2)
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2) + currentFrame.getRegionWidth() / 2, body.getPosition().y * PPM - (height / 2), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        else
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2) + currentFrame.getRegionWidth(), body.getPosition().y * PPM - (height / 2), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());

        updateAttack(batch);

        batch.end();

        if (body.getLinearVelocity().x != 0 && !canAttack) {
            if (xForce < 0 && body.getLinearVelocity().y == 0.0) {
                setCurrentAnimation(RUNNING);
            } else if (xForce > 0 && body.getLinearVelocity().y == 0.0) {
                setCurrentAnimation(RUNNING);
            } else if (xForce == 0 && body.getLinearVelocity().y == 0.0) {
                setCurrentAnimation(IDLE);
            }
        } else if (!canAttack)
            setCurrentAnimation(IDLE);

        if (body.getLinearVelocity().y > 0.0 && !canAttack) {
            this.currentAnimation = JUMPING;
            stateTime = 0;
            stepIndex = 0;
        } else if (body.getLinearVelocity().y < 0.0 && !canAttack) {
            this.currentAnimation = FALLING;
            stateTime = 0;
            stepIndex = 0;
        }

        body.setLinearVelocity(xForce * 5, body.getLinearVelocity().y);
    }

    public void updateAttack(SpriteBatch batch) {
        if (currentMagicPoints > 0) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !throwPowerBall && currentMagicPoints - MAGIC >= 0) {
                canAttack = true;
                attack1 = true;
                setCurrentAnimation(ATTACK1);
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !attack1 && currentMagicPoints - POWERBALL >= 0) {
                canAttack = true;
                throwPowerBall = true;
                setCurrentAnimation(ATTACK2);
            }
        }

        if (animations.get(ATTACK1).isAnimationFinished(stateTime) || animations.get(ATTACK2).isAnimationFinished(stateTime)) {
            canAttack = false;
        }
        if (animations.get(ATTACK2).isAnimationFinished(stateTime) && throwPowerBall) {
            powerBalls.add(new PowerBall(world, getX(), getY() + 15, 20, runningRight, 35));
            currentMagicPoints -= POWERBALL;
            throwPowerBall = false;
        }
        if (animations.get(ATTACK1).isAnimationFinished(stateTime) && attack1) {
            magicks.add(new Magic(world, getX(), getY() + 20, 25, runningRight));
            currentMagicPoints -= MAGIC;
            attack1 = false;
        }
        DrawPowerBall(batch);

        for (Magic magic : magicks) {
            magic.update(Gdx.graphics.getDeltaTime());
        }

        for (int i = 0; i < magicks.size && !magicks.isEmpty(); i++) {
            if (magicks.get(i).toBeDestroyed) {
                bodiesToDestroy.add(magicks.get(i).body);
                magicks.removeIndex(i);
            }
        }
    }

    public void DrawPowerBall(SpriteBatch batch) {
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies)
            if (body.getUserData() == "POWERBALL") {
                imgPowerBall.setPosition(body.getPosition().x * PPM - imgPowerBall.getWidth() / 2, body.getPosition().y * PPM - imgPowerBall.getHeight() / 2);
                imgPowerBall.setWidth(20);
                imgPowerBall.setHeight(20);
                imgPowerBall.draw(batch, 1);
            }

        for (PowerBall powerBall : powerBalls) {
            powerBall.update(Gdx.graphics.getDeltaTime());
        }


        for (int i = 0; i < powerBalls.size && !powerBalls.isEmpty(); i++) {
            if (powerBalls.get(i).toBeDestroyed) {
                bodiesToDestroy.add(powerBalls.get(i).body);
                powerBalls.removeIndex(i);
            }
        }



    }

    public void setX(float x) {
        body.setTransform(x / PPM, getY() / PPM, 0);
    }

    public void setY(float y) {
        body.setTransform(getX() / PPM, y / PPM, 0);
    }

    public int getMaxExpPoints() {
        return expPoints;
    }

    public int getCurrentExpPoints() {
        return currentExpPoints;
    }
}
