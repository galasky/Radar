package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Administrateur on 16/07/2014.
 */

public class Info {
    private SpriteBatch myBatch;
    private ShapeRenderer shapeDebugger;
    private GUIController guiController;
    private MyFont font;
    private float scrolling, inertie;

    public Info(GUIController gui) {
        font = new MyFont("font/HelveticaNeue.ttf", 40);
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
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
        shapeDebugger.rect(-Gdx.graphics.getWidth() / 2,  Gdx.graphics.getHeight() / 3, Gdx.graphics.getWidth(), -Gdx.graphics.getHeight() / 2);
        shapeDebugger.end();
        myBatch.end();
        myBatch.begin();
        font.draw(myBatch, "Radar Copyright expert-its, Lambert-Tesserenc et Regis MARTY", 40, Gdx.graphics.getHeight() / 2);
        font.draw(myBatch, "Version : 0.0.8 Beta", 40, Gdx.graphics.getHeight() / 2 + 80);
        font.draw(myBatch, "Date de build : 17/07/2014 17h47", 40, Gdx.graphics.getHeight() / 2 + 80 * 2);
        font.draw(myBatch, "commit : Version 0.0.8 Zoom tactile", 40, Gdx.graphics.getHeight() / 2 + 80 * 3);
        myBatch.end();
    }
}