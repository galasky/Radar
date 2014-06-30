package com.mygdx.radar.android;

import java.util.List;

public class Trajet {
    private int _type;
    private String _ligne, _destination;
    private List<StopTimes> _listStopTimes;

    public Trajet(int type, String ligne, String destintation, List<StopTimes> listStopTimes) {
        _type = type;
        _ligne = ligne;
        _destination = destintation;
        _listStopTimes = listStopTimes;
    }
}
