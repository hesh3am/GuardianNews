package com.example.hesham.guardiannews;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesham on 20-Aug-18.
 */

public class QueryUtilits {
    /////////////////////////////Empty constractor no way to make an object from this class ;)
    private QueryUtilits() {
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            /////////////////////////////////////// Make a Request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            /////////////////////////////////////// Make a Connection
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.d("Error response code: ", String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<Guardian> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Guardian> guardianList = extractFeatureFromJson(jsonResponse);

        return guardianList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            ////////////////////////////// All the input stored as a Buffer to Read AS a package
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Guardian> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<Guardian> guardianArrayList = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            /////////////////////////////////////// Access JSON
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentResults = resultsArray.getJSONObject(i);
                /////////////////////////////////////// Access data
                String date = currentResults.getString("webPublicationDate");
                /////////////////////////////////////// Access sectionName
                String category = currentResults.getString("sectionName");
                /////////////////////////////////////// Access webUrl
                String url = currentResults.getString("webUrl");
                /////////////////////////////////////// Access webTittle
                String Title = currentResults.getString("webTitle");

                JSONArray tagsauthor = currentResults.getJSONArray("tags");
                String author = ".....";
                /////////////////////////////////////// Access Author if Found
                if (tagsauthor.length() != 0) {
                    JSONObject currenttagsauthor = tagsauthor.getJSONObject(0);
                    author = currenttagsauthor.getString("webTitle");
                } else {
                    author = "No Author Found";
                }

                Guardian guardianNews = new Guardian(Title, category, date, url, author);

                guardianArrayList.add(guardianNews);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guardianArrayList;
    }
}