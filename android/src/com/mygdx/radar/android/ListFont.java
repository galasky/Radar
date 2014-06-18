package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Administrateur on 16/06/2014.
 */
public class ListFont {
    private SpriteBatch myBatch;
    private ShapeRenderer shapeDebugger;
    private GUIController guiController;
    private ArrayList<MyFont>   _listFont;
    private float scrolling, inertie;

    public ListFont(GUIController gui) {
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        _listFont = FontManager.instance()._listFont;
        scrolling = 0;
        inertie = 0;
    }

    public void touch(float x, float y, float deltaX, float deltaY) {
        inertie = -deltaY;
    }

    public void render() {
        scrolling += inertie;
        inertie /= 1.05;
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
        shapeDebugger.rect(-Gdx.graphics.getWidth() / 2,  Gdx.graphics.getHeight() / 2 - 100, Gdx.graphics.getWidth(), -Gdx.graphics.getHeight() + 100);
        shapeDebugger.end();
        myBatch.end();
        myBatch.begin();
        for (int i = 0; i < _listFont.size(); i++)
        {
            MyFont font = _listFont.get(i);
            font.draw(myBatch, "8 min marche -> 8 min 12:25", 50, 100 + 100 * i + scrolling);
            font.draw(myBatch, font.font, Gdx.graphics.getWidth() / 2 + 80, 100 + 100 * i + scrolling);
        }
        myBatch.end();
    }
}
