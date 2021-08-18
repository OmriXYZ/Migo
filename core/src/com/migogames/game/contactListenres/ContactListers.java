package com.migogames.game.contactListenres;

import com.badlogic.gdx.physics.box2d.*;
import com.migogames.game.Player;

public class ContactListers implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        boolean aIsPlayer = fa.getUserData().getClass() == Player.class;
        boolean bIsPlayer = fb.getUserData().getClass() == Player.class;
        boolean aIsGround = fa.getUserData() == "Floor";
        boolean bIsGround = fb.getUserData() == "Floor";


        if (fa == null || fb == null)
            return;
        if (fa.getUserData() == null || fb.getUserData() == null)
            return;


        if (aIsPlayer && bIsGround) {
            Player tfa;
            tfa = (Player) fa.getUserData();
            tfa.hitTheGround();
        }
        if (bIsPlayer && aIsGround) {
            Player tfb;
            tfb = (Player) fb.getUserData();
            tfb.hitTheGround();
        }


    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null)
            return;
        if (fa.getUserData() == null || fb.getUserData() == null)
            return;

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }


    private boolean isContact(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Player || b.getUserData() instanceof Player) {
            if (a.getUserData() instanceof Shape || b.getUserData() instanceof Shape)
                return true;
        }
        return false;
    }
}
