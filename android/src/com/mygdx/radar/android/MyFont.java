package com.mygdx.radar.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Administrateur on 06/06/2014.
 */
public class MyFont {
    private BitmapFont      _font;
    public String   font;

    FreeTypeFontGenerator generator;

    public MyFont(String str, int size) {
        font = str;
        FreeTypeFontGenerator.FreeTypeFontParameter paramatre = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramatre.size = size;
        generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        _font = generator.generateFont(paramatre);
        _font.setColor(Color.WHITE);
        generator.dispose();

    }

    public void draw(SpriteBatch batch, String str, float x, float y) {
        _font.draw(batch, str, x, y);
    }
}
