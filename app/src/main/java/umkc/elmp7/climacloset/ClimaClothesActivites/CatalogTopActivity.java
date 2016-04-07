package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Intent;
import android.database.Cursor;
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

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.Exceptions.AddItemException;
import umkc.elmp7.climacloset.R;
import umkc.elmp7.climacloset.ClimaUtil.*;

public class CatalogTopActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private EditText topTypeET, minTempET, maxTempET;
    private Spinner colorET, sleeveTypeET;
    private Button photoButton, populate;
    private ImageView photoPreview;
    private Bitmap photo;
    private ClimaClosetDB DB;
    private static ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_top);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.climacloset);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Keep keyboard from auto appearing on activity launch
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get various elements
        this.photoPreview = (ImageView) this.findViewById(R.id.photoPreviewTop);
        this.photoButton = (Button) this.findViewById(R.id.photoTopButton);
        this.colorET = (Spinner) this.findViewById(R.id.colorTopTextField);
        this.sleeveTypeET = (Spinner) this.findViewById(R.id.sleeveTypeSpinner);
        this.topTypeET = (EditText) this.findViewById(R.id.topTypeTextField);
        this.minTempET = (EditText) this.findViewById(R.id.mintempTop);
        this.maxTempET = (EditText) this.findViewById(R.id.maxtempTop);
        this.populate = (Button) this.findViewById(R.id.button);

        //to be removed
        Button submitButton = (Button) this.findViewById(R.id.submitTop);

        //Initialize database
        DB = new ClimaClosetDB(getApplicationContext());

        //Build color and sleeve type spinners
        buildSpinner();

        //LISTENERS SECTION
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DB.addTop(new ClimaClosetTop(photo,
                            getResources().getString(R.string.DB_AVAIL),
                            colorET.getSelectedItem().toString(),
                            topTypeET.getText().toString(),
                            Double.parseDouble(minTempET.getText().toString()),
                            Double.parseDouble(maxTempET.getText().toString()),
                            sleeveTypeET.getSelectedItem().toString()));
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
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

//        //to be removed
//        populate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Cursor cursor = DB.ClimaQueryTop(0.0);
//                cursor.moveToFirst();
//                topTypeET.setText(cursor.getString(cursor.getColumnIndex(DB.SHIRTS_KEY_TOP_TYPE)));
//                photoPreview.setImageBitmap(ClimaUtilities.getCursorImage(cursor, DB.SHIRTS_KEY_PICTURE));
//                photoPreview.setEnabled(true);
//
//            }
//        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            this.photo = (Bitmap) data.getExtras().get("data");
            photoPreview.setImageBitmap(photo);
            photoButton.setText("Retake Photo");
        }
    }
    public void clearFields(){
        colorET.setSelection(0);
        topTypeET.setText("");
        minTempET.setText("");
        maxTempET.setText("");
        sleeveTypeET.setSelection(0);
        photoPreview.setImageBitmap(null);
        photoButton.setText("Take Photo");
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
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sleeveArray )
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
        sleeveTypeET.setAdapter(spinnerAdapter);
    }
}
