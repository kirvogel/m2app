package com.example.data;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONCreator {
    public static JSONObject createJSONObject(String input) throws JSONException {
        return new JSONObject(input);
    }
    public static JSONObject[] createArray(int i) {
        return new JSONObject[i];
    }
}
