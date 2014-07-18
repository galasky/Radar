package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Info {
    private SpriteBatch myBatch;
    private ShapeRenderer shapeDebugger;
    private GUIController guiController;
    private MyFont font;

    public Info(GUIController gui) {
        font = new MyFont("font/HelveticaNeue.ttf", 40);
        guiController = gui;
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
    }

    public void render() {
        myBatch.begin();
        Gdx.gl20.glLineWidth(10);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeDebugger.setProjectionMatrix(guiController.camera.combined);
        shapeDebugger.begin(ShapeRenderer.ShapeType.Line);
        shapeDebugger.setColor(0f, 0f, 0f, 0.8f);
        shapeDebugger.end();
        shapeDebugger.begin(ShapeRenderer.ShapeType.Filled);
        shapeDebugger.rect(-Gdx.graphics.getWidth() / 2,  567 / 2, Gdx.graphics.getWidth(), -567);
        shapeDebugger.end();
        myBatch.end();
        myBatch.begin();
        font.draw(myBatch, "Vincent Morin, Lambert-Tesserenc et Regis MARTY", 40, Gdx.graphics.getHeight() / 2 -142);
        font.draw(myBatch, "Radar Copyright expert-its,", 40, Gdx.graphics.getHeight() / 2 + 80 -142);
        font.draw(myBatch, "Version : 0.0.8 Beta", 40, Gdx.graphics.getHeight() / 2 + 80 * 2 -142);
        font.draw(myBatch, "Date de build : 17/07/2014 17h47", 40, Gdx.graphics.getHeight() / 2 + 80 * 3 -142);
        font.draw(myBatch, "commit : Version 0.0.8 Reduction Taille fenetre + slide ", 40, Gdx.graphics.getHeight() / 2 + 80 * 4 -142);
        myBatch.end();
    }
}