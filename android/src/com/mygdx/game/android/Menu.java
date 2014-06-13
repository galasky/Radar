package com.mygdx.game.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Administrateur on 06/06/2014.
 */
public class Menu {
    public boolean active;
    private SpriteBatch myBatch;
    private ShapeRenderer shapeDebugger;
    private GUIController guiController;
    private int slide;

    public Menu(GUIController gui) {
        slide = 0;
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        active = false;
    }

    public void active() {
        active = true;
        slide = 0;
    }

    private void update() {
        if (active)
        {
            if (slide < 200)
                slide++;
        }
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
            shapeDebugger.setColor(0f, 0f, 0f, 0.80f);
            shapeDebugger.end();
            shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
            shapeDebugger.rect(0, slide, Gdx.graphics.getWidth(), 200);
            shapeDebugger.end();
            myBatch.end();
        }
    }
}
