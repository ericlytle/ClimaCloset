package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.OpenWeather.OpenWeatherService;
import umkc.elmp7.climacloset.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final String DEFAULTCITYNAME = "Kansas%20City,MO";
    private double lon, lat;
    Button catalogbottomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button catalogTopButton = (Button) findViewById(R.id.catalogTopButton);
        catalogbottomButton = (Button) findViewById(R.id.catalogBottomButton);
        Button browseTopsButton = (Button) findViewById(R.id.browseTopsButton);
        Button browseBottomButton = (Button) findViewById(R.id.browseBottomsButton);

        //EditText cityStateName = (EditText) findViewById(R.id.cityState);
        //cityStateName.setText(DEFAULTCITYNAME);

        new FetchWeatherTask().execute(DEFAULTCITYNAME);

        catalogTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent catalogTopIntent = new Intent(getApplicationContext(), CatalogTopActivity.class);
                startActivity(catalogTopIntent);
            }
        });

        catalogbottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent catalogBottomIntent = new Intent(getApplicationContext(), CatalogBottomActivity.class);
                startActivity(catalogBottomIntent);
            }
        });

        browseTopsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseTopsActivity = new Intent(getApplicationContext(), BrowseTopsActivity.class);
                startActivity(browseTopsActivity);
            }
        });
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        browseBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browseBottomActivity = new Intent(getApplicationContext(), BrowseBottomsActivity.class);
                startActivity(browseBottomActivity);
            }
        });



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener()
        {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double speed = location.getSpeed(); //speed in meter/minute
                speed = (speed*3600)/1000;      // speed in km/minute
                try {
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0)
                        System.out.println(addresses.get(0).getLocality());
                    //Toast.makeText(findViewById(android.R.id.content), "Current speed:" + location.getSpeed(),Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Current lat:" + addresses.get(0).getLocality() + addresses.get(0).getAdminArea(), Snackbar.LENGTH_LONG)
                            .show();
                    new FetchWeatherTask().execute(ClimaUtilities.parseSpaces(addresses.get(0).getLocality()) + "," + addresses.get(0).getAdminArea());
                }
                catch (Exception e)
                {}
            }
        };

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);}
        catch(SecurityException e){
            Snackbar.make(findViewById(android.R.id.content), "Permission problems", Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        // Not sure what the three dots mean? See: http://stackoverflow.com/questions/3158730/java-3-dots-in-parameters?rq=1
        protected String doInBackground(String... parms) {

            // parms[0] is first parm, etc.
            OpenWeatherService weatherService = new OpenWeatherService(parms[0]);
            try {
                ClimaUtilities.temperature = weatherService.getTemperature();
                return ClimaUtilities.temperature;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Not available";
        }

        protected void onProgressUpdate(Void... values) {

        }

        //  invoked on the UI thread after the background computation finishes
        protected void onPostExecute(String temperature) {
            // Example assert statements
            //Assert.assertNull(temperature);
            Assert.assertNotNull("Error: temperature is null", temperature);

            TextView tempDisp = (TextView) findViewById(R.id.temp);
            tempDisp.setText(temperature + (char) 0x00B0 + "F" );
            //catalogbottomButton.setText(String.valueOf(lon));
            //updateUI(temperature);
        }
    }
}
