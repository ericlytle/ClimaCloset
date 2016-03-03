package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.Assert;

import umkc.elmp7.climacloset.OpenWeather.OpenWeatherService;
import umkc.elmp7.climacloset.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final String DEFAULTCITYNAME = "Kansas%20City,MO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button catalogTopButton = (Button) findViewById(R.id.catalogTopButton);
        Button catalogbottomButton = (Button) findViewById(R.id.catalogBottomButton);
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

        browseBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseBottomActivity = new Intent(getApplicationContext(), BrowseBottomsActivity.class);
                startActivity(browseBottomActivity);
            }
        });

    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        // Not sure what the three dots mean? See: http://stackoverflow.com/questions/3158730/java-3-dots-in-parameters?rq=1
        protected String doInBackground(String... parms) {

            // parms[0] is first parm, etc.
            OpenWeatherService weatherService = new OpenWeatherService(parms[0]);
            try {
                String temperature = weatherService.getTemperature();
                return temperature;
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
            Assert.assertTrue(3 < 4);

            TextView tempDisp = (TextView) findViewById(R.id.temp);
            tempDisp.setText(temperature);
            //updateUI(temperature);
        }
    }
}
