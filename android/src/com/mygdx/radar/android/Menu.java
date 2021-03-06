package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.radar.MyGdxRadar;

/**
 * Created by Administrateur on 06/06/2014.
 */
public class Menu {
    public boolean active, desactive;
    public boolean opened;
    private SpriteBatch myBatch;
    private ShapeRenderer shapeDebugger;
    private GUIController guiController;
    private int         slide, maxSlide;
    private PushButton  parametres;
    private PushButton  info;
    private boolean     showInfo;
    private MyFont      font;
    private ListFont    listFont;

    public class ActionParametres implements IAction {
        public void exec() {
            if (listFont == null)
                listFont = new ListFont(guiController);
            else
                listFont = null;
        }
    }

    public class ActionInfo implements IAction {
        public void exec() {
            if (showInfo)
                showInfo = false;
            else
                showInfo = true;
        }
    }

    public Menu(GUIController gui) {
        listFont = null;
        font = new MyFont("font/HelveticaNeue.ttf", 20);
        showInfo = false;
        maxSlide = 100;
        slide = 0;
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        parametres = new PushButton(new ActionParametres(), new Texture(Gdx.files.internal("texture/parametres.png")), new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 60), new Vector2(80, 80));
        info = new PushButton(new ActionInfo(), new Texture(Gdx.files.internal("texture/info_icon.png")), new Vector2(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() + 60), new Vector2(80, 80));
        active = false;
        desactive = false;
        opened = false;
    }

    public void active() {
        active = true;
    }

    public void desactive() {
        desactive = true;
    }

    public void touch(float x, float y, float deltaX, float deltaY) {
        if (listFont != null)
        {
            listFont.touch(x, y, deltaX, deltaY);
        }
    }

    private void update() {
        if (active)
        {
            if (slide < maxSlide)
                slide += 10;
            else
            {
                opened = true;
                active = false;
            }
        }
        if (desactive)
        {
            if (slide > 0)
                slide -= 10;
            else
            {
                listFont = null;
                opened = false;
                desactive = false;
            }
        }
        parametres.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 60 - slide);
        info.setPosition(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() + 60 - slide);
    }

    public void tap(float x, float y) {
        parametres.tap(x, y);
        info.tap(x, y);
    }

    private void showInfo(){
        shapeDebugger.rect(-Gdx.graphics.getWidth() / 2,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
        font.draw(myBatch, "Radar vii.jj Copyright expert-its, Lambert-Tesserenc et Regis MARTY", 40, Gdx.graphics.getHeight() / 2);
    }

    public void render() {

        update();
        if (active || slide > 0)
        {
            myBatch.begin();
            Gdx.gl20.glLineWidth(10);

            //Enable transparency
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeDebugger.setProjectionMatrix(guiController.camera.combined);
            shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
            shapeDebugger.setColor(0f, 0f, 0f, 1f);
            shapeDebugger.end();
            shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
            shapeDebugger.rect(-Gdx.graphics.getWidth() / 2,  Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), -slide);
            if (showInfo)
                showInfo();
            shapeDebugger.end();
            myBatch.end();
            parametres.draw();
            info.draw();
            if (listFont != null)
                listFont.render();
        }
    }
}
