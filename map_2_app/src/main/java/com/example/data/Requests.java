package com.example.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

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

public class Requests {

    private Requests() {

    }

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, int[]> map = new HashMap<>();
    private static Map<String, Integer> mapNames = new HashMap<>();

    static {
        mapNames.put("ru", 250);
        map.put(250, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 19, 20,
                23, 28, 32, 35, 37, 38, 39, 44, 50, 92, 93, 99}); //россия

    }

    public static int[] getMNCList(int mcc) {
        int[] res = map.get(mcc);
        if (res == null) return new int[]{};
        return res;

    }

    public static Integer getMCC(String name) {
        if (name == null) return 0;
        Integer res = mapNames.get(name);
        if (res == null) return 0;
        return res;
    }

    public static String getCountryCode(double latitude, double longtitude) throws IOException {
        URL url = new URL("https://us1.unwiredlabs.com/v2/reverse.php?token="
                + "21f162e748cdff"
                + "&lat=" + latitude
                + "&lon=" + longtitude);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } catch (IOException e) {
            Timber.tag("M2APP").v(e);
        } finally {
            urlConnection.disconnect();
        }
        return "";
    }

    public static String[] getStations(int cellId, int lac, String country) throws IOException {
        Integer code = getMCC(country);
        int[] mncList = getMNCList(code);
        List<String> resList = new LinkedList<>();

        for (int mnc :
                mncList) {
            URL url = new URL("https://us1.unwiredlabs.com/v2/process.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setReadTimeout(10000);
            try (Writer writer = new OutputStreamWriter(connection.getOutputStream());
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String query = "{" +
                        "\"token\": \"21f162e748cdff\"," +
                        "\"radio\": \"gsm\"," +
                        "\"mcc\": " + code + "," +
                        "\"mnc\": " + mnc + "," +
                        "\"cells\": [{" +
                        "\"cid\": " + cellId + "," +
                        "\"lac\":" + lac +
                        "}]," +
                        "\"address\": 0" +
                        "}";

                writer.write(query);
                //writer.flush();

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                resList.add(response.toString());
            } catch (IOException e) {
                Timber.tag("M2APP").v(e);
            } finally {
                connection.disconnect();
            }
        }
        String[] objects = new String[resList.size()];
        for (int i = 0; i < resList.size(); ++i) {
            objects[i] = resList.get(i);
        }
        return objects;
    }

    public static MyResult getCellId() {
        int cellId = 0;
        int lac = 0;
        final TelephonyManager telephony = getManager();
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

    private static TelephonyManager getManager() {
        return
                (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    }
}
