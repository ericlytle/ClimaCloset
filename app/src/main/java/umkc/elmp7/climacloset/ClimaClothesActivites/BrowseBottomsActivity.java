package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.DeleteItemException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Exceptions.UpdateException;
import umkc.elmp7.climacloset.R;

public class BrowseBottomsActivity extends AppCompatActivity {
    private final String WHITESPACE = " ";
    private ClimaClosetDB DB;
    private TextView bottomTypeTextView, colorTextView, minTempTextView, maxTempTextView;
    private Button deleteBottomButton, markDirtyButton;
    private Cursor cursor;
    private ImageView imageView;
    private ClimaClosetBottom tempBottom, temp;
    private LinearLayout linearLayout;
    private static ArrayAdapter<String> spinnerAdapter;
    private Spinner filterSpinner;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_bottoms);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialize textviews
        bottomTypeTextView = (TextView) findViewById(R.id.bottomTypeTextView);
        colorTextView = (TextView) findViewById(R.id.colorBottomTextView);
        minTempTextView  = (TextView) findViewById(R.id.minTempBottomTextView);
        maxTempTextView = (TextView) findViewById(R.id.maxTempBottomTextView);

        //Initialize Buttons
        deleteBottomButton = (Button) findViewById(R.id.deleteBottomButton);
        markDirtyButton = (Button) findViewById(R.id.markDirtyBottomButton);

        //Ititialize spinner
        filterSpinner = (Spinner) findViewById(R.id.filterClothingSpinner);

        //Initialize Database
        DB = ClimaClosetDB.instance(getApplicationContext());

        //Initialize linear layout
        linearLayout = (LinearLayout) findViewById(R.id.browseBottomLayout);

        //Build spinner and set selection to first item
        buildSpinner();
        filterSpinner.setSelection(0);
        filterSpinner.setOnItemSelectedListener(itemSelectedListener);

        //Load pictures corresponding to item selected in the spinner(All)
        loadPictures(filterSpinner.getSelectedItem().toString());

        //Initialize button click listeners
        toolbar.setNavigationOnClickListener(backButtonClickListener);
        markDirtyButton.setOnClickListener(markItemDirtyButtonClickListener);
        deleteBottomButton.setOnClickListener(deleteBottomButtonClickListener);
    }

    private void loadPictures(String availability){

        linearLayout.removeAllViews();

        try {
            cursor = DB.ClimaQueryBottom(Double.parseDouble(ClimaUtilities.temperature), availability);
        }
        catch (QueryException e){
            Log.d("BrowseBottoms", e.getMessage());
        }
        catch (AvailabilityException e){
            Log.d("BrowseBottoms", e.getMessage());
        }

        //populate items into imageview from cursor object
        while(cursor.moveToNext()){
            tempBottom = new ClimaClosetBottom(ClimaUtilities.getCursorImage(cursor, DB.BOTTOMS_KEY_PICTURE),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_AVAILABILITY),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_COLOR),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_TYPE),
                    ClimaUtilities.getCursorDouble(cursor, DB.BOTTOMS_KEY_MIN_TEMP),
                    ClimaUtilities.getCursorDouble(cursor, DB.BOTTOMS_KEY_MAX_TEMP),
                    ClimaUtilities.getCursorLong(cursor, DB.SHIRTS_KEY_ID));
            imageView = new ImageView(this);
            imageView.setImageBitmap(tempBottom.getPicture());
            imageView.setMinimumHeight(getResources().getInteger(R.integer.image_height));
            imageView.setMinimumWidth(getResources().getInteger(R.integer.image_width));
            imageView.setTag(tempBottom);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp = (ClimaClosetBottom) v.getTag();
                    bottomTypeTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_bottomTypeDisplay) + WHITESPACE + temp.getBottomType());
                    colorTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_colorDisplay) + WHITESPACE + temp.getColor());
                    minTempTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
                    maxTempTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(temp.getID()), Snackbar.LENGTH_LONG)
                            .show();
                    if (temp.getAvailability().equalsIgnoreCase("Avail")) {
                        markDirtyButton.setText("Mark Dirty");
                    }
                    if (temp.getAvailability().equalsIgnoreCase("nAvail")) {
                        markDirtyButton.setText("Mark Clean");
                    }
                    deleteBottomButton.setVisibility(View.VISIBLE);
                    markDirtyButton.setVisibility(View.VISIBLE);
                }
            });

            linearLayout.addView(imageView);
        }

        if (linearLayout.getChildCount() == 0) {
            deleteBottomButton.setVisibility(View.INVISIBLE);
            markDirtyButton.setVisibility(View.INVISIBLE);
        }

        else {
            deleteBottomButton.setVisibility(View.VISIBLE);
            markDirtyButton.setVisibility(View.VISIBLE);
        }
    }

    private void clearFields(){
        bottomTypeTextView.setText("");
        colorTextView.setText("");
        minTempTextView.setText("");
        maxTempTextView.setText("");
        deleteBottomButton.setVisibility(View.INVISIBLE);
        markDirtyButton.setVisibility(View.INVISIBLE);
    }

    void buildSpinner(){
        String[] colorArray = getResources().getStringArray(R.array.clothingFilter);
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
        filterSpinner.setAdapter(spinnerAdapter);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            loadPictures(filterSpinner.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };

    private View.OnClickListener markItemDirtyButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            try{
                if (temp != null)
                {
                    switch (temp.getAvailability()){
                        case ("Avail"):
                            temp.updateAvailability("nAvail");
                            break;
                        case ("nAvail"):
                            temp.updateAvailability("Avail");
                            break;
                    }
                }
                DB.markBottomItemDirty(temp, DB.BOTTOMS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item updated!");
            }
            catch (UpdateException e)
            {
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item not updated!");
            }
        }
    };

    private View.OnClickListener deleteBottomButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            try {
                DB.deleteItem(temp, DB.BOTTOMS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item deleted successfully");
            } catch (DeleteItemException e) {
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item not deleted successfully");
                Log.d("DeleteItemException", e.getMessage());
            }
        }
    };

    private View.OnClickListener backButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent homeScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeScreen);
        }
    };
}
