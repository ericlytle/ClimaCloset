package umkc.elmp7.climacloset.JSON;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONService {

    private static final String TAG = JSONService.class.getName();

    public static JSONObject getJSONObject(String uri) throws JSONException {
        return new JSONObject(getJSONString(uri));
    }

    public static String getJSONString(String uri) {

        StringBuilder builder = new StringBuilder();
        try {

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            if (response == 200) {
                InputStream content = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(TAG, "Failed to download file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

//    public static String getJSONStringold(String uri) {
//
//        StringBuilder builder = new StringBuilder();
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(uri);
//        try {
//            HttpResponse response = client.execute(httpGet);
//            StatusLine statusLine = response.getStatusLine();
//            int statusCode = statusLine.getStatusCode();
//            if (statusCode == 200) {
//                HttpEntity entity = response.getEntity();
//                InputStream content = entity.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    builder.append(line);
//                }
//            } else {
//                Log.e(TAG, "Failed to download file");
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return builder.toString();
//    }
}
