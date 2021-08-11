package com.migogames.game.contactListenres;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.migogames.game.MapBodyBuilder;
import com.migogames.game.Platform;
import com.migogames.game.Player;

public class ContactListers implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null)
            return;
        if(fa.getUserData() == null || fb.getUserData() == null)
            return;

        if(isContact(fa, fb)){
            if(fa.getUserData().getClass() == Player.class ) {
                Player tfa;
                tfa = (Player) fa.getUserData();
                tfa.hit();
            }
            if(fb.getUserData().getClass() == Player.class ) {
                Player tfb;
                tfb = (Player) fb.getUserData();
                tfb.hit();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null)
            return;
        if(fa.getUserData() == null || fb.getUserData() == null)
            return;

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }


    private boolean isContact(Fixture a, Fixture b){
        if(a.getUserData() instanceof Player || b.getUserData() instanceof Player) {
            if (a.getUserData() instanceof Shape || b.getUserData() instanceof Shape)
                 return true;
        }
        return false;
    }
}
