package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.app.Activity;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.AddItemException;
import umkc.elmp7.climacloset.Listeners.BackButtonClickListener;
import umkc.elmp7.climacloset.R;

public class CatalogTopActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private EditText etTopType, etMinTemp, etMaxTemp;
    private Spinner colorSpinner, sleeveTypeSpinner;
    private Button btnTakePhoto, btnSubmit;
    private ImageView ivPhotoPreview;
    private Bitmap photoDisplay;
    private ClimaClosetDB climaClosetDB;
    private Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_top);
        myActivity = this;
        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.climatoolbarsmall);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new BackButtonClickListener(this));

        //Keep keyboard from auto appearing on activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get various elements
        ivPhotoPreview = (ImageView) findViewById(R.id.photoPreviewTop);
        btnTakePhoto = (Button) findViewById(R.id.photoTopButton);
        colorSpinner = (Spinner) findViewById(R.id.colorTopTextField);
        sleeveTypeSpinner = (Spinner) findViewById(R.id.sleeveTypeSpinner);
        etTopType = (EditText) findViewById(R.id.topTypeTextField);
        etMinTemp = (EditText) findViewById(R.id.mintempTop);
        etMaxTemp = (EditText) findViewById(R.id.maxtempTop);

        //to be removed
        btnSubmit = (Button) findViewById(R.id.submitTop);

        //Initialize database
        climaClosetDB = ClimaClosetDB.instance(getApplicationContext());

        //Build color and sleeve type spinners
        buildSpinner();

        //LISTENERS SECTION
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    climaClosetDB.addTop(new ClimaClosetTop(photoDisplay,
                            getResources().getString(R.string.DB_AVAIL),
                            colorSpinner.getSelectedItem().toString(),
                            etTopType.getText().toString(),
                            Double.parseDouble(etMinTemp.getText().toString()),
                            Double.parseDouble(etMaxTemp.getText().toString()),
                            sleeveTypeSpinner.getSelectedItem().toString()));
                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.Top_catalog_success),
                            Snackbar.LENGTH_LONG)
                            .show();
                    clearFields();
                }
                catch (AddItemException e) {
                    Log.d("AddItemException", e.getMessage());
                    Snackbar.make(findViewById(android.R.id.content),
                            getResources().getString(R.string.Top_catalog_failure),
                            Snackbar.LENGTH_LONG)
                            .show();
                }
                catch (Exception e){
                    ClimaUtilities.AlertMessage(myActivity, "Something went wrong!\n" +
                            "Check to make sure all fields are populated!");
                }
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photoDisplay = (Bitmap) data.getExtras().get("data");
            ivPhotoPreview.setImageBitmap(photoDisplay);
            btnTakePhoto.setText(getResources().getString(R.string.retake_photo_button));
        }
    }
    public void clearFields(){
        colorSpinner.setSelection(0);
        etTopType.setText("");
        etMinTemp.setText("");
        etMaxTemp.setText("");
        sleeveTypeSpinner.setSelection(0);
        ivPhotoPreview.setImageBitmap(null);
        btnTakePhoto.setText(getResources().getString(R.string.take_photo_button));
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    void buildSpinner(){
        String[] colorArray = getResources().getStringArray(R.array.color_array);
        String[] sleeveArray = getResources().getStringArray(R.array.sleeve_array);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colorArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.WHITE);

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
        colorSpinner.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sleeveArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.WHITE);

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
        sleeveTypeSpinner.setAdapter(spinnerAdapter);
    }
}
