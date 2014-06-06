package com.mygdx.game.android;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrateur on 05/06/2014.
 */
public class PushButton {
    private SpriteBatch _spriteBatch;
    private BitmapFont _font;
    public Vector2 position;
    public String text;
    private float time;
    public boolean select;
    private Timer timer;

    public PushButton(String t, Vector2 p) {
        select = false;
        text = t;
        position = p;
        _spriteBatch = new SpriteBatch();
        _font = new BitmapFont();
        _font.setColor(Color.WHITE);
        _font.setScale(3F, 3F);
        timer = new Timer();
    }


    public void setSelect(String str) {
        text = "Change Font : " + str;
        _font.setColor(Color.RED);
        select = true;
        time = 0;

    }

    public boolean colision(float x, float y) {
        return (x >= position.x && x <= position.x + 40 * text.length() && y >= position.y - 50 && y <= position.y + 50);
    }

    public void draw() {
        _spriteBatch.begin();
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