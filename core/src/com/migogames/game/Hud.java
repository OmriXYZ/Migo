package com.migogames.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Hud {
    private Stage stage;
    private FitViewport stageViewport;
    private Table table;
    private Group healthGroup, manaGroup, expGroup;
    private Image imgHealth, imgMana, imgExp, imgBlankBarHP, imgBlankBarMP, imgBlankExp;
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
        Texture texExp = new Texture("hud/exp.png");
        imgExp = new Image(texExp);
        Texture texBlankBar = new Texture("hud/blank.png");
        imgBlankBarMP = new Image(texBlankBar);
        imgBlankBarHP = new Image(texBlankBar);
        imgBlankExp = new Image(texBlankBar);

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

        expGroup = new Group();
        expGroup.addActor(imgBlankExp);
        expGroup.addActor(imgExp);
        expGroup.setWidth(imgBlankExp.getWidth());
        expGroup.setHeight(imgBlankExp.getHeight());
        imgExp.setPosition(4f,3.4f);

        font = new BitmapFont(Gdx.files.internal("hud/font.fnt"), false);
        font.getData().setScale(0.5f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        table = new Table();
        table.setWidth(stage.getWidth());
        table.setHeight(100);
        table.align(Align.left);
        table.pad(0, 8,0,0);
        table.setPosition(0, Gdx.graphics.getHeight()-table.getHeight());

        table.add(healthGroup).padBottom(5);
        table.row();
        table.add(manaGroup).padBottom(5);
        table.row();
        table.add(expGroup);
        table.row();


       //stage.setDebugAll(true);
        stage.addActor(table);
    }
    public Stage getStage() {
        return stage;
    }

    public void updateHud(int hp, int mp, int exp, int cHP, int cMP, int cExp, SpriteBatch hudBatch) {
        imgHealth.setWidth(getCurrentHP(hp, cHP));
        imgHealth.setHeight(20f);
        imgMana.setWidth(getCurrentMP(mp, cMP));
        imgMana.setHeight(20);
        imgBlankBarMP.setWidth(WIDTHBAR+8);
        imgBlankBarMP.setHeight(26f);
        imgBlankBarHP.setWidth(WIDTHBAR+8);
        imgBlankBarHP.setHeight(26f);
        imgBlankExp.setWidth(WIDTHBAR+8);
        imgBlankExp.setHeight(26f);
        imgExp.setWidth(getCurrentExp(exp, cExp));
        imgExp.setHeight(20);
        hudBatch.begin();
        font.draw(hudBatch, "HP: " + cHP, healthGroup.getX() + imgHealth.getX() + table.getX() ,healthGroup.getY() + table.getY() + healthGroup.getHeight() - imgHealth.getY()*2);
        font.draw(hudBatch, "MP: " + cMP, manaGroup.getX() + imgMana.getX() + table.getX() ,manaGroup.getY() + table.getY() + manaGroup.getHeight() - imgMana.getY()*2);
        font.draw(hudBatch, "EXP: " + cExp, expGroup.getX() + imgExp.getX() + table.getX() ,expGroup.getY() + table.getY() + expGroup.getHeight() - imgExp.getY()*2);
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

    public int getCurrentExp(int exp, int cExp) {
        return (int)((double)cExp / exp * WIDTHBAR);
    }
}
