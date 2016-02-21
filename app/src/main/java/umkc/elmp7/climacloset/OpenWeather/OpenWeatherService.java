package umkc.elmp7.climacloset.OpenWeather;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import umkc.elmp7.climacloset.JSON.JSONService;

public class OpenWeatherService {
    private String uri;
    private static final String TAG = OpenWeatherService.class.getName();

    // cityName is city,state. For example: Leawood,KS or Kansas%20City,MO
    public OpenWeatherService(String cityName) {
        uri = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=imperial&appid=e1fdab04f806cbc652a01aed29256fd9";
    }

    // Throws Exception if there is a problem getting temperature
    public String getTemperature() throws Exception {
        try {
            JSONObject weatherData = JSONService.getJSONObject(uri);
            Log.i(TAG, weatherData.toString());
            JSONObject mainSection = weatherData.getJSONObject("main");
            String temperature = mainSection.getString("temp");
            return temperature;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new Exception("Problem in OpenWeatherService.getTemperature()");
        }
    }
}
