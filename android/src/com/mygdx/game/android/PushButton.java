package com.mygdx.game.android;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrateur on 05/06/2014.
 */
public class PushButton {
    private Sprite sprite;
    private Texture texture;
    private SpriteBatch _spriteBatch;
    private BitmapFont _font;
    public Vector2 position;
    public String text;
    private float time;
    private IAction action;
    public boolean select;

    public PushButton(IAction a, String t, Vector2 p) {
        action = a;
        select = false;
        text = t;
        position = p;
        _spriteBatch = new SpriteBatch();
        sprite = null;
        _font = new BitmapFont();
        _font.setColor(Color.WHITE);
        _font.setScale(3F, 3F);
        // loading a texture from image file
        texture = null;
    }

    public PushButton(IAction a, Texture t, Vector2 p) {
        action = a;
        select = false;
        text = "";
        position = p;
        _spriteBatch = new SpriteBatch();

        _font = new BitmapFont();
        _font.setColor(Color.WHITE);
        _font.setScale(3F, 3F);
        // loading a texture from image file
        texture = t;

// setting a filter is optional, default = Nearest
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


// binding texture to sprite and setting some attributes
        sprite = new Sprite(texture);
        sprite.setOrigin(t.getWidth() / 2, t.getHeight() / 2);
        sprite.setSize(100, 100);
        sprite.setPosition(position.x - t.getWidth() / 2, position.y - t.getHeight() / 2);
    }

    public void tap(float x, float y) {
        if (colision(x, y))
        {
            _font.setColor(Color.RED);
            select = true;
            action.exec();
            time = 0;
        }
    }

    public void setSelect(String str) {
        text = "Change Font : " + str;
        _font.setColor(Color.RED);
        select = true;
        action.exec();
        time = 0;

    }

    public boolean colision(float x, float y) {
        return (x >= position.x && x <= position.x + 40 * text.length() && y >= position.y - 50 && y <= position.y + 50);
    }

    public void draw() {
        _spriteBatch.begin();
        if (sprite != null)
            sprite.draw(_spriteBatch);
        _font.draw(_spriteBatch, text, position.x, position.y);
        _spriteBatch.end();
        if (select)
        {
            time += Gdx.graphics.getDeltaTime();
            if (time > 0.1)
            {
                _font.setColor(Color.WHITE);
                select = false;
            }
        }
    }
}