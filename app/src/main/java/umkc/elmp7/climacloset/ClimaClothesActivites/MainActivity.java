package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.List;
import java.util.Locale;

import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Listeners.ActivityButtonClickListener;
import umkc.elmp7.climacloset.OpenWeather.OpenWeatherService;
import umkc.elmp7.climacloset.R;

public class MainActivity extends AppCompatActivity {

    private static boolean LOCATION_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnCatalogTop = (Button) findViewById(R.id.catalogTopButton);
        Button btnCatalogBottom = (Button) findViewById(R.id.catalogBottomButton);
        Button btnBrowseTops = (Button) findViewById(R.id.browseTopsButton);
        Button btnBrowseBottoms = (Button) findViewById(R.id.browseBottomsButton);

        btnCatalogTop.setOnClickListener(new ActivityButtonClickListener(this, CatalogTopActivity.class));

        btnCatalogBottom.setOnClickListener(new ActivityButtonClickListener(this, CatalogBottomActivity.class));

        btnBrowseTops.setOnClickListener(new ActivityButtonClickListener(this, BrowseTopsActivity.class));

        btnBrowseBottoms.setOnClickListener(new ActivityButtonClickListener(this, BrowseBottomsActivity.class));

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (LOCATION_CHANGED){
            TextView tempDisp = (TextView) findViewById(R.id.temp);
            tempDisp.setText(ClimaUtilities.temperature + (char) 0x00B0 + "F" );
        }

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
                LOCATION_CHANGED = true;
                String address;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                try {
                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                    address = addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea();
                    ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Current Address:" + address);
                    new FetchWeatherTask().execute(ClimaUtilities.parseSpaces(address));
                }
                catch (Exception e)
                {}
            }
        };


        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);}
        catch(SecurityException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There were problems with getting your GPS location. This App filters clothing solely on GPS Location.\nCheck permissions in:" +
                "\nSettings->Apps->ClimaCloset->Permissions")
                .setCancelable(false)
                .setPositiveButton("Take me there!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myAppSettings);
                    }
                })
                .setNegativeButton("Okay", null);
            AlertDialog alert = builder.create();
            alert.show();
        }



    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

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
            Assert.assertNotNull("Error: temperature is null", temperature);
            TextView tempDisp = (TextView) findViewById(R.id.temp);
            tempDisp.setText(temperature + (char) 0x00B0 + "F" );
        }
    }
}
