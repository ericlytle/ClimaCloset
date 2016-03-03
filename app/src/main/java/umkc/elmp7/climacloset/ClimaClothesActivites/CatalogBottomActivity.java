package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.R;

public class CatalogBottomActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private EditText minTempET, maxTempET, bottomTypeET;
    private Spinner colorET;
    private Button photoButton, submitButton;
    private ImageView photoPreview;
    private Bitmap photo;
    private ClimaClosetDB DB;
    private static ArrayAdapter<String> spinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_bottom);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Keep keyboard from auto appearing on activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get various elements
        this.photoPreview = (ImageView) this.findViewById(R.id.photoPreviewBottom);
        this.photoButton = (Button) this.findViewById(R.id.photoBottomButton);
        this.colorET = (Spinner) this.findViewById(R.id.colorBottomSpinner);
        this.bottomTypeET = (EditText) this.findViewById(R.id.bottomTypeTextField);
        this.minTempET = (EditText) this.findViewById(R.id.mintempBottom);
        this.maxTempET = (EditText) this.findViewById(R.id.maxtempBottom);
        this.submitButton = (Button) this.findViewById(R.id.submitBottom);

        //Initialize database
        DB = new ClimaClosetDB(getApplicationContext());

        //Build color and sleeve type spinners
        buildSpinner();

        //LISTENERS SECTION
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                boolean success = DB.addBottom(new ClimaClosetBottom(photo,
                        getResources().getString(R.string.DB_AVAIL),
                        colorET.getSelectedItem().toString(),
                        bottomTypeET.getText().toString(),
                        Double.parseDouble(minTempET.getText().toString()),
                        Double.parseDouble(maxTempET.getText().toString())));
                if (success) {
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.Bottom_catalog_success), Snackbar.LENGTH_LONG)
                            .show();
                    clearFields();
                }
                else{
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.Bottom_catalog_failure), Snackbar.LENGTH_LONG)
                            .show();
                }
            } catch (Exception e) {
                Log.d("submitOnClick", e.getMessage());
            }
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            this.photo = (Bitmap) data.getExtras().get("data");
            photoPreview.setImageBitmap(photo);
            photoButton.setText(getResources().getString(R.string.retake_photo_button));
        }
    }
    public void clearFields(){
        colorET.setSelection(0);
        bottomTypeET.setText("");
        minTempET.setText("");
        maxTempET.setText("");
        photoPreview.setImageBitmap(null);
        photoButton.setText(getResources().getString(R.string.take_photo_button));
    }

    void buildSpinner(){
        String[] colorArray = getResources().getStringArray(R.array.color_array);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colorArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView ) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTextColor(Color.BLACK);
                ((TextView) view).setTextSize(20);
                if (position % 2 == 0) { // we're on an even row
                    view.setBackgroundColor(Color.WHITE);
                } else {
                    view.setBackgroundColor(Color.LTGRAY);
                }
                return view;
            }

        };
        colorET.setAdapter(spinnerAdapter);
    }
}
