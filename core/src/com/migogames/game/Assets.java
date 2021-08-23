package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    public AssetManager assetManager;
    public static BitmapFont fontDmg;

    public void load() {
        if (assetManager == null) {
            assetManager = new AssetManager();
        }
        assetManager.load("player/skin.atlas", TextureAtlas.class);
        assetManager.load("enemy/evilwizard/skin.atlas", TextureAtlas.class);

        fontDmg = new BitmapFont(Gdx.files.internal("hud/font.fnt"), false);
        fontDmg.getData().setScale(0.5f);
        fontDmg.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        fontDmg.setColor(Color.RED);
    }


}
