package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Administrateur on 26/06/2014.
 */
public class BubbleDrawer {

    public SpriteBatch     _spriteBatch;
    public ShapeRenderer   shapeDebugger;
    public Texture         tWalking;
    public Sprite          sWalking;
    public MyFont          fontNum, fontStationName, fontWalk;
    public SpriteBatch     myBatch;
    public Color           green, orange, red, grey;
    public Sprite           sprite;
    public MyFont           _font;
    public Texture          texture;

    private BubbleDrawer() {
        fontNum = new MyFont("font/HelveticaNeueCondensedBold.ttf", 48);
        fontStationName = new MyFont("font/HelveticaNeue.ttf", 48);
        fontWalk = new MyFont("font/HelveticaNeueBold.ttf", 48);
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        _spriteBatch = new SpriteBatch();
        green = new Color().set(129 / 255f, 215 / 255f, 89 / 255f, 1f);
        orange = new Color().set(230 / 255f, 163 / 255f, 64 / 255f, 1f);
        red = new Color().set(210 / 255f, 90 / 255f, 87 / 255f, 1f);
        grey = new Color().set(83 / 255f, 88 / 255f, 95 / 255f, 1f);
        tWalking = new Texture(Gdx.files.internal("texture/walking-green.png"));
        tWalking.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sWalking = new Sprite(tWalking);
        sWalking.setSize(40, 60);
        _font = new MyFont("font/HelveticaNeue.ttf", 40);
        texture = new Texture(Gdx.files.internal("texture/bus.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
        sprite.setSize(64, 64);
    }

    public static BubbleDrawer instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        /** Instance unique non préinitialisée */
        private final static BubbleDrawer instance = new BubbleDrawer();
    }
}
