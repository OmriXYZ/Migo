package com.migogames.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import static com.migogames.game.Constants.PPM;

public class Enemy extends Entity {

    private Array<Body> tmpBodies = new Array<Body>();

    private int xForce;

    private boolean isDie = false;

    private Timer timer;
    private Timer.Task hitTask;

    public Enemy(String name, World world, SpriteBatch batch, float x, float y, int width, int height, Skin skin, int hp, int mp, int cHP, int cMP) {
        super(name, world, batch, x, y, width, height, skin, hp, mp, cHP, cMP);

        body.setUserData("ENEMY");

        addTexture(skin.getRegion("run"), 8, 0.065f);
        addTexture(skin.getRegion("idle"), 8, 0.065f);
        addTexture(skin.getRegion("hit"), 3, 0.08f);

        setCurrentAnimation(IDLE);

        xForce = 0;

        timer = new Timer();
        hitTask = new Timer.Task() {
            @Override
            public void run() {
                System.out.println("zubi");
            }
        };

    }

    @Override
    public void update(SpriteBatch batch) {
        super.update(batch);
        updateMovementTexture(batch);
    }

    public void updateMovementTexture(SpriteBatch batch) {
        currentFrame = (TextureRegion) animations.get(currentAnimation).getKeyFrame(stateTime, true);

        isDie = currentHealthPoints <= 0;

        if (isDie)
            die();


        xForce = 0;

        if (animations.get(2).isAnimationFinished(stateTime) && !isDie) {
            System.out.println(!isDie);
            System.out.println("die");
            if (body.getLinearVelocity().x > 0) {
                runningRight = true;
                setCurrentAnimation(RUNNING);
            }
            else if (body.getLinearVelocity().x < 0) {
                runningRight = false;
                setCurrentAnimation(RUNNING);
            }
            else {
                setCurrentAnimation(IDLE);
            }
        }


        batch.begin();
        if (runningRight)
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2), body.getPosition().y * PPM - (height / 2), currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        else if (currentAnimation == ATTACK1 || currentAnimation == ATTACK2 || currentAnimation ==  IDLE)
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2) + currentFrame.getRegionWidth() / 2, body.getPosition().y * PPM - (height / 2), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        else
            batch.draw(currentFrame, body.getPosition().x * PPM - (width / 2) + currentFrame.getRegionWidth(), body.getPosition().y * PPM - (height / 2), -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        batch.end();

        if (!isDie)
            updateAttack(batch);

        body.setLinearVelocity(xForce * 5, body.getLinearVelocity().y);
    }

    private void updateAttack(SpriteBatch batch) {
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() == "PLAYER") {
                if (distanceX(body.getPosition().x) < 25) {
                    attackPlayer(body.getPosition().x);
                }
            }

        }
    }

    private float distanceX(float playerX) {
        return Math.abs(playerX - this.body.getPosition().x);
    }

    private void attackPlayer(float playerX) {
        if (distanceX(playerX) > 3) {
            if (playerX > getX()/32) {
                xForce = 1;
            }
            else
                xForce = -1;
        }
    }

    public void getHit(int hitPoints) {
        currentHealthPoints -= hitPoints;
        setCurrentAnimation(2);
        batch.begin();
        for (int i=0; i<5000; i++)
        Assets.fontDmg.draw(batch, hitPoints+"", getX(), getY());
        batch.end();
    }

    private void die() {

    }

}
