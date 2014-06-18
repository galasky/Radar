package com.mygdx.radar.android;

import java.util.ArrayList;

/**
 * Created by Administrateur on 17/06/2014.
 */
public class FontManager {

    public ArrayList<MyFont> _listFont;

    private FontManager() {
        loadFont();
    }

    public static FontManager instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private final static FontManager instance = new FontManager();
    }

    private void loadFont()
    {
        int size = 40;
        _listFont = new ArrayList<MyFont>();
        _listFont.add(new MyFont("font/HelveticaNeue.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueBold.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueBoldItalic.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueCondensedBlack.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueCondensedBold.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueItalic.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueLight.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueLightItalic.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueMedium.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueUltraLight.ttf", size));
        _listFont.add(new MyFont("font/HelveticaNeueUltraLightItalic.ttf", size));
    }
}
