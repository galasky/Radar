package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Menu {
    public boolean          active, desactive;
    public boolean          opened;
    private SpriteBatch     myBatch;
    private Info            info;
    private ShapeRenderer   shapeDebugger;
    private GUIController   guiController;
    private int             slide, maxSlide;
    private PushButton      parametres;
    private PushButton      Buttoninfo;
    private boolean         showInfo;

    public class ActionParametres implements IAction {
        public void exec() {

        }
    }

    public class ActionInfo implements IAction {
        public void exec() {
            showInfo = !showInfo;
        }
    }

    public Menu(GUIController gui) {
        info = new Info(gui);
        showInfo = false;
        maxSlide = 100;
        slide = 0;
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        parametres = new PushButton(new ActionParametres(), new Texture(Gdx.files.internal("texture/parametres.png")), new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 60), new Vector2(80, 80));
        Buttoninfo = new PushButton(new ActionInfo(), new Texture(Gdx.files.internal("texture/info_icon.png")), new Vector2(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() + 60), new Vector2(80, 80));
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
                opened = false;
                desactive = false;
            }
        }
        parametres.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 60 - slide);
        Buttoninfo.setPosition(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() + 60 - slide);
    }

    public void tap(float x, float y) {
        parametres.tap(x, y);
        Buttoninfo.tap(x, y);
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
                info.render();
            shapeDebugger.end();
            myBatch.end();
            parametres.draw();
            Buttoninfo.draw();
        }
    }
}
