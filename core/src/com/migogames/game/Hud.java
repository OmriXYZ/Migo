package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Hud {
    private Stage stage;
    private FitViewport stageViewport;
    private Image imgHealth, imgMana, imgBlankBarHP, imgBlankBarMP;
    private Label lblHP, lblMP;
    private BitmapFont font;
    private final float X = 200,Y = 895;
    private final int WIDTHBAR = 500;

    public Hud(SpriteBatch hudBatch) {
        stageViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(stageViewport, hudBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor

        Texture texHP = new Texture("hud/health.png");
        imgHealth = new Image(texHP);
        Texture texMP = new Texture("hud/mana.png");
        imgMana = new Image(texMP);
        Texture texBlankBar = new Texture("hud/blank.png");
        imgBlankBarMP = new Image(texBlankBar);
        imgBlankBarHP = new Image(texBlankBar);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        lblHP = new Label("e", skin);
        lblMP = new Label("e", skin);

        font = new BitmapFont(Gdx.files.internal("hud/font.fnt"), false);
        font.getData().setScale(0.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);



        Table table = new Table();
        table.add(imgBlankBarMP);
        table.add(imgBlankBarHP);
        table.add(imgHealth);
        table.add(imgMana);

        stage.addActor(table);
    }
    public Stage getStage() {
        return stage;
    }

    public void updateHud(int hp, int mp, int cHP, int cMP, SpriteBatch hudBatch) {
        imgHealth.setWidth(getCurrentHP(hp, cHP));
        imgHealth.setHeight(19f);
        imgHealth.setPosition(4, 40 + Y);
        imgMana.setWidth(getCurrentMP(mp, cMP));
        imgMana.setHeight(20);
        imgMana.setPosition(4, 10 + Y);
        imgBlankBarMP.setWidth(WIDTHBAR+8);
        imgBlankBarMP.setHeight(26f);
        imgBlankBarMP.setPosition(0, 7f + Y);
        imgBlankBarHP.setWidth(WIDTHBAR+8);
        imgBlankBarHP.setHeight(26f);
        imgBlankBarHP.setPosition(0, 37f + Y);

        hudBatch.begin();
        font.draw(hudBatch, "HP: " + cHP, 6 ,57 + Y);
        font.draw(hudBatch, "MP: " + cMP, 6 ,27 + Y);
        hudBatch.end();

    }

    public void dispose(){
        stage.dispose();
    }

    public int getCurrentHP(int hp, int cHP) {
        return (int)((double)cHP / hp * WIDTHBAR);
    }

    public int getCurrentMP(int mp, int cMP) {

        return (int)((double)cMP / mp * WIDTHBAR);
    }
}
