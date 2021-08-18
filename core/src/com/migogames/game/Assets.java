package com.migogames.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    public AssetManager assetManager;

    public void load() {
        if (assetManager == null) {
            assetManager = new AssetManager();
        }
        assetManager.load("skins.atlas", TextureAtlas.class);
    }


}
