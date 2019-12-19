package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MobileCountryCodeMobileNetworkCode {
    @SuppressLint("UseSparseArrays")
    private static Map<Integer, int[]> map = new HashMap<>();
    private static Map<String, Integer> mapNames = new HashMap<>();

    private MobileCountryCodeMobileNetworkCode() {

    }

    static {
        mapNames.put("ru", 250);
        map.put(250, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 19, 20,
        23, 28, 32, 35, 37, 38, 39, 44, 50, 92, 93, 99}); //россия

    }

    private static int[] getMNCList(int mcc) {
        try {
            return map.get(mcc);
        } catch (Exception e) {
            return new int[]{};
        }
    }

    private static Integer getMCC(String name) {
        if (name == null) return 0;
        try {
            return mapNames.get(name);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @SuppressLint("Assert")
    private static String getCoutryName(double latitude, double longtitude) throws IOException {
        URL url = new URL("https://us1.unwiredlabs.com/v2/reverse.php?token="
                + "a65aee3fdcc744"
                + "&lat=" + latitude
                + "&lon=" + longtitude);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            assert false;
            JSONObject json = new JSONObject(response.toString());
            return (String)((JSONObject)json.get("address")).get("country_code");
        } catch (IOException | JSONException e) {
            Timber.tag("M2APP").v(e);
        } finally {
            urlConnection.disconnect();
        }
        return "";
    }

    @SuppressLint("Assert")
    public static JSONObject[] getStations(double latitude, double longtitude) throws IOException {
        String country = getCoutryName(latitude, longtitude);
        Integer code = getMCC(country);
        int[] mncList = getMNCList(code);
        MyResult res = getCellId();
        int cellId = res.getFirst();
        int lac = res.getSecond();

        List<JSONObject> obj = new LinkedList<>();

        for (int mnc :
                mncList) {
            URL url = new URL("https://us1.unwiredlabs.com/v2/process.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setReadTimeout(10000);
            try (Writer writer = new OutputStreamWriter(connection.getOutputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String query = "{\n" +
                        "    \"token\": \"a65aee3fdcc744\"," +
                        "    \"radio\": \"gsm\"," +
                        "    \"mcc\": " + code + "," +
                        "    \"mnc\": " + mnc + "," +
                        "    \"cells\": [{" +
                        "        \"cid\": " + cellId + "," +
                        "\"lac\":" + lac +
                        "    }]," +
                        "    \"address\": 0" +
                        "}";

                writer.write(query);
                writer.flush();

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                assert false;
                JSONObject json = new JSONObject(response.toString());
                if (json.get("status").equals("ok")) {
                    obj.add(json);
                }
            } catch (IOException | JSONException e) {
                Timber.tag("M2APP").v(e);
            } finally {
                connection.disconnect();
            }
        }
        JSONObject[] objects = new JSONObject[obj.size()];
        for (int i = 0; i < obj.size(); ++i) {
            objects[i] = obj.get(i);
        }
        return objects;
    }

    private static MyResult getCellId() {
        int cellId = 0;
        int lac = 0;
        final TelephonyManager telephony =
                (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        assert telephony != null;
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            @SuppressLint("MissingPermission") final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
            if (location != null) {
                cellId = location.getCid();
                lac = location.getLac();
            }
        }
        return new MyResult(cellId, lac);
    }
}
