package com.mygdx.radar.android;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadStation extends Thread {
    public boolean filtre;
    private String result;
    List<Station> listStation;

    public LoadStation() {
        listStation = new ArrayList<Station>();
        filtre = false;
    }

    @Override
    public void run() {
        while (Game3D.instance().modelStation == null || You.instance().coordinate == null)
            ;
        if (filtre) {
               StationManager.instance().unSelectBubble();
               StationManager.instance().filtreDistance();
        }
        else {
            if (Config.instance().onLine)
                new RequetAPI().execute();
            else
                StationManager.instance().addListStop(Territory.instance().getListStopByDistance(Config.instance().distance3, You.instance().coordinate));
        }
    }

    public class ParseJSON extends AsyncTask<Void, Void, Void> {

        private void parseJson(String s) throws JSONException {
            JSONObject obj = new JSONObject(s);

            JSONArray listStationJS = obj.getJSONArray("listStation");
            for (int i = 0; i < listStationJS.length(); i++)
            {
                Station station = new Station();
                station.station_id = listStationJS.getJSONObject(i).getString("station_id");
                station.name = listStationJS.getJSONObject(i).getString("name");
                station.coord = new CoordinateGPS();
                station.coord.longitude = listStationJS.getJSONObject(i).getDouble("longitude");
                station.coord.latitude = listStationJS.getJSONObject(i).getDouble("latitude");
                Log.d("ok ", "JSON " + station.name + " longitude " + station.coord.longitude + " latitude " + station.coord.latitude);
                JSONArray listLigneJS = listStationJS.getJSONObject(i).getJSONArray("listLigne");
                for (int u = 0; u < listLigneJS.length(); u++)
                {
                    Stop stop = new Stop();
                    stop.line = listLigneJS.getJSONObject(u).getString("line");
                    stop.destination = listLigneJS.getJSONObject(u).getString("destination");
                    if (stop.destination.length() >= 20)
                        stop.destination = stop.destination.substring(0, 19);
                    JSONArray listTimeJS = listLigneJS.getJSONObject(u).getJSONArray("listTime");
                    for (int y = 0; y < listTimeJS.length(); y++)
                    {
                        StopTimes stopTimes = new StopTimes();
                        stopTimes.departure_time = new MyTimes(listTimeJS.getJSONObject(y).getString("time"));
                        stop.listStopTimes.add(stopTimes);
                    }
                    station.stops.add(stop);
                }
                listStation.add(station);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                parseJson(result);
                StationManager.instance().addListStation(listStation);
                World.instance().statu = 1;
            } catch (JSONException e) {
                World.instance().statu = -3;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
        }
    }

    public class RequetAPI extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient httpclient = new DefaultHttpClient();
            try
            {
                HttpGet httpget = new HttpGet(Config.instance().urlAPI + "?longitude=" + You.instance().coordinate.longitude + "&latitude=" + You.instance().coordinate.latitude + "&distance=" + Config.instance().distance3);
                ResponseHandler responseHandler = new BasicResponseHandler();
                result = (String) httpclient.execute(httpget, responseHandler);
                Log.d("ok", "HTTP " + result);
            }
            catch (ClientProtocolException e)
            {
                result = "server error";
                Log.d("ok", "HTTP 1" + e.toString());
                e.printStackTrace();
            }
            catch (IOException e)
            {
                result = "internet error";
                Log.d("ok", "HTTP 2" + e.toString());
                e.printStackTrace();
            }
            finally
            {
                httpclient.getConnectionManager().shutdown();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            if (result.equals("server error"))
            {
                World.instance().statu = -1;
            }
            else if (result.equals("internet error"))
            {
                World.instance().statu = -2;
            }
            else {
                    new ParseJSON().execute();
//                    parseJson(result);
            }
        }
    }
}
