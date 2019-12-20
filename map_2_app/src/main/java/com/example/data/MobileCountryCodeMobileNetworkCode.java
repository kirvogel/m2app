package com.example.data;

import android.annotation.SuppressLint;

import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class MobileCountryCodeMobileNetworkCode {

    private MobileCountryCodeMobileNetworkCode() {

    }

    @SuppressLint("Assert")
    public static String getCoutryName(double latitude, double longtitude) {
        try  {
            JSONObject json = JSONCreator.createJSONObject(Requests.getCountryCode(latitude, longtitude));
            return (String)((JSONObject)json.get("address")).get("country_code");
        } catch (Exception e) {
            Timber.tag("M2APP").v(e);
        }
        return "";
    }



    @SuppressLint("Assert")
    public static JSONObject[] getStations(double latitude, double longtitude) throws IOException {

        MyResult res = Requests.getCellId();
        String[] stations = Requests.getStations(res.getFirst(),res.getSecond(), getCoutryName(latitude, longtitude));
        List<JSONObject> obj = new LinkedList<>();

        for (String mnc :
                stations) {
            try  {

                JSONObject json = JSONCreator.createJSONObject(mnc);
                if (json.get("status").equals("ok")) {
                    obj.add(json);
                }
            } catch (JSONException e) {
                Timber.tag("M2APP").v(e);
            }
        }
        JSONObject[] objects = JSONCreator.createArray(obj.size());
        for (int i = 0; i < obj.size(); ++i) {
            objects[i] = obj.get(i);
        }
        return objects;
    }

}
