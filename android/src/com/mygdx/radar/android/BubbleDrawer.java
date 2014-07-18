package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BubbleDrawer {

    public SpriteBatch     _spriteBatch;
    public ShapeRenderer   shapeDebugger;
    public Texture         tWalking, tHorloge;
    public Sprite          sWalking, sHorloge;
    public MyFont          fontNum, fontStationName, fontWalk;
    public SpriteBatch     myBatch;
    public Color           green, orange, red, grey, blue, cercle1, cercle2, transparence;
    public Sprite          sprite;
    public MyFont          _font;
    public Texture         texture;

    private BubbleDrawer() {
        fontNum = new MyFont("font/HelveticaNeueCondensedBold.ttf", 48);
        fontStationName = new MyFont("font/HelveticaNeue.ttf", 48);
        fontWalk = new MyFont("font/HelveticaNeueBold.ttf", 48);
        myBatch = new SpriteBatch();
        shapeDebugger = new ShapeRenderer();
        _spriteBatch = new SpriteBatch();
        green = new Color().set(129 / 255f, 215 / 255f, 89 / 255f, 1);
        orange = new Color().set(230 / 255f, 163 / 255f, 64 / 255f, 1);
        red = new Color().set(210 / 255f, 90 / 255f, 87 / 255f, 1);
        grey = new Color().set(83 / 255f, 88 / 255f, 95 / 255f, 1);
        blue = new Color().set(147 / 255f, 199 / 255f, 255 / 255f, 1);
        cercle1 = new Color().set(241 / 255f, 196 / 255f, 15 / 255f, 1);
        cercle2 = new Color().set(243 / 255f, 156 / 255f, 18 / 255f, 1);
        transparence = new Color().set(1, 0, 0, 0);
        tWalking = new Texture(Gdx.files.internal("texture/walking-green.png"));
        tWalking.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tHorloge = new Texture(Gdx.files.internal("texture/horloge.png"));
        tHorloge.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sWalking = new Sprite(tWalking);
        sWalking.setSize(40, 60);
        sHorloge = new Sprite(tHorloge);
        sHorloge.setSize(50, 50);
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
