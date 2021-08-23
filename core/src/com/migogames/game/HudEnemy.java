package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HudEnemy {
    private Stage stage;
    private FitViewport stageViewport;
    private Table table;
    private Group healthGroup, manaGroup;
    private Image imgHealth, imgMana, imgBlankBarHP, imgBlankBarMP;
    private BitmapFont font;
    private final int WIDTHBAR = 50;
    private final int HEIGHTBAR = 10;


    public HudEnemy(SpriteBatch hudBatch, OrthographicCamera camera) {
        stageViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageViewport.setCamera(camera);
        stage = new Stage(stageViewport, hudBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor

        Texture texHP = new Texture("hud/health.png");
        imgHealth = new Image(texHP);
        Texture texMP = new Texture("hud/mana.png");
        imgMana = new Image(texMP);

        Texture texBlankBar = new Texture("hud/blank.png");
        imgBlankBarMP = new Image(texBlankBar);
        imgBlankBarHP = new Image(texBlankBar);

        healthGroup = new Group();
        healthGroup.addActor(imgBlankBarHP);
        healthGroup.addActor(imgHealth);
        healthGroup.setWidth(imgBlankBarHP.getWidth());
        healthGroup.setHeight(imgBlankBarHP.getHeight());
        imgHealth.setPosition(4f,3.4f);

        manaGroup = new Group();
        manaGroup.addActor(imgBlankBarMP);
        manaGroup.addActor(imgMana);
        manaGroup.setWidth(imgBlankBarMP.getWidth());
        manaGroup.setHeight(imgBlankBarMP.getHeight());
        imgMana.setPosition(4f,3.4f);


        font = new BitmapFont(Gdx.files.internal("hud/font.fnt"), false);
        font.getData().setScale(0.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        table = new Table();
        table.align(Align.left);

        table.add(healthGroup).padBottom(-10);
        table.row();
        table.add(manaGroup);
        table.row();


        //stage.setDebugAll(true);
        stage.addActor(table);
    }
    public Stage getStage() {
        return stage;
    }

    public void updateHud(int hp, int mp, int cHP, int cMP, Vector2 enemyPos, SpriteBatch hudBatch) {
        table.setPosition(enemyPos.x, enemyPos.y);
        imgHealth.setWidth(getCurrentHP(hp, cHP));
        imgHealth.setHeight(HEIGHTBAR-6);
        imgMana.setWidth(getCurrentMP(mp, cMP));
        imgMana.setHeight(HEIGHTBAR-6);
        imgBlankBarMP.setWidth(WIDTHBAR+8);
        imgBlankBarMP.setHeight(HEIGHTBAR);
        imgBlankBarHP.setWidth(WIDTHBAR+8);
        imgBlankBarHP.setHeight(HEIGHTBAR);
//        hudBatch.begin();
//        font.draw(hudBatch, "HP: " + cHP, healthGroup.getX() + imgHealth.getX() + table.getX() ,healthGroup.getY() + table.getY() + healthGroup.getHeight() - imgHealth.getY()*2);
//        font.draw(hudBatch, "MP: " + cMP, manaGroup.getX() + imgMana.getX() + table.getX() ,manaGroup.getY() + table.getY() + manaGroup.getHeight() - imgMana.getY()*2);
//        hudBatch.end();

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
