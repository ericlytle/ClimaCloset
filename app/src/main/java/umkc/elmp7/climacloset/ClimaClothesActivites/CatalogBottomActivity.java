package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.AddItemException;
import umkc.elmp7.climacloset.Listeners.BackButtonClickListener;
import umkc.elmp7.climacloset.Listeners.TakePhotoButtonClickListener;
import umkc.elmp7.climacloset.R;

public class CatalogBottomActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private EditText etMinTemp, etMaxTemp, etBottomType;
    private Spinner colorSpinner;
    private Button btnTakePhoto, btnSubmit;
    private ImageView ivPhotoPreview;
    private Bitmap photoDisplay;
    private ClimaClosetDB climaClosetDB;
    private Activity myActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_bottom);
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
        ivPhotoPreview = (ImageView) findViewById(R.id.photoPreviewBottom);
        btnTakePhoto = (Button) findViewById(R.id.photoBottomButton);
        colorSpinner = (Spinner) findViewById(R.id.colorBottomSpinner);
        etBottomType = (EditText) findViewById(R.id.bottomTypeTextField);
        etMinTemp = (EditText) findViewById(R.id.mintempBottom);
        etMaxTemp = (EditText) findViewById(R.id.maxtempBottom);
        btnSubmit = (Button) findViewById(R.id.submitBottom);

        //Initialize database
        climaClosetDB = ClimaClosetDB.instance(getApplicationContext());

        //Build color and sleeve type spinners
        buildSpinner();

        //LISTENERS SECTION
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    climaClosetDB.addBottom(new ClimaClosetBottom(photoDisplay,
                            getResources().getString(R.string.DB_AVAIL),
                            colorSpinner.getSelectedItem().toString(),
                            etBottomType.getText().toString(),
                            Double.parseDouble(etMinTemp.getText().toString()),
                            Double.parseDouble(etMaxTemp.getText().toString())));
                    ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content),
                            getResources().getString(R.string.Bottom_catalog_success));
                    clearFields();
                } catch (AddItemException e) {
                    Log.d("AddItemException", e.getMessage());
                }
                catch (Exception e){
                    ClimaUtilities.AlertMessage(myActivity, "Something went wrong!\n" +
                            "Check to make sure all fields are populated!");
                }
            }
        });

        btnTakePhoto.setOnClickListener(new TakePhotoButtonClickListener(this));
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
        etBottomType.setText("");
        etMinTemp.setText("");
        etMaxTemp.setText("");
        ivPhotoPreview.setImageBitmap(null);
        btnTakePhoto.setText(getResources().getString(R.string.take_photo_button));
    }

    void buildSpinner(){
        String[] colorArray = getResources().getStringArray(R.array.color_array);
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
    }
}
