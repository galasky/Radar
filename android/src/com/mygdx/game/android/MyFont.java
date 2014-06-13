package com.mygdx.game.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Administrateur on 06/06/2014.
 */
public class MyFont {
    public BitmapFont      _font;
    public String   font;
    FreeTypeFontGenerator generator;

    public MyFont(String str) {
        font = str;
        FreeTypeFontGenerator.FreeTypeFontParameter paramatre = new FreeTypeFontGenerator.FreeTypeFontParameter();

        generator = new FreeTypeFontGenerator(Gdx.files.internal(font));
        _font = generator.generateFont(paramatre);
    }
}
