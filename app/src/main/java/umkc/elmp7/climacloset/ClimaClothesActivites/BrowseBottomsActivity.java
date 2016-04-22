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

import java.util.HashMap;
import java.util.Map;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.ClimaUtil.DetailsLoader;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.DeleteItemException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Exceptions.UpdateException;
import umkc.elmp7.climacloset.R;

public class BrowseBottomsActivity extends AppCompatActivity {
    private ClimaClosetDB DB;
    private TextView bottomTypeTextView, colorTextView, minTempTextView, maxTempTextView;
    private Button deleteBottomButton, markDirtyButton;
    private Map<String, TextView> textViewMap;
    private Cursor cursor;
    private LinearLayout linearLayout;
    private static ArrayAdapter<String> spinnerAdapter;
    private Spinner filterSpinner;
    public void onCreate(Bundle savedInstanceState) {
        textViewMap = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_bottoms);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(backButtonClickListener);
        getSupportActionBar().setIcon(R.drawable.climatoolbarsmall);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialize textviews into map
        textViewMap.put(ClimaClosetDB.BOTTOMS_KEY_TYPE, (TextView)findViewById(R.id.bottomTypeTextView));
        textViewMap.put(ClimaClosetDB.BOTTOMS_KEY_COLOR, (TextView)findViewById(R.id.colorBottomTextView));
        textViewMap.put(ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP, (TextView)findViewById(R.id.minTempBottomTextView));
        textViewMap.put(ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP, (TextView)findViewById(R.id.maxTempBottomTextView));
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
        ClimaUtilities.buildSpinner(filterSpinner, getApplicationContext());
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
        DetailsLoader detailsLoader;
        //done in details loader? linearLayout.removeAllViews();
        try {
            cursor = DB.ClimaQueryBottom(Double.parseDouble(ClimaUtilities.temperature), availability);
            detailsLoader = new DetailsLoader(cursor, linearLayout, deleteBottomButton, markDirtyButton, textViewMap, this);
            detailsLoader.LoadPictures();
        }
        catch (QueryException e){
            Log.d("BrowseBottoms", e.getMessage());
        }
        catch (AvailabilityException e){
            Log.d("BrowseBottoms", e.getMessage());
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
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_COLOR).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP).setText("");
        deleteBottomButton.setVisibility(View.INVISIBLE);
        markDirtyButton.setVisibility(View.INVISIBLE);
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
            ClimaClosetBottom bottomTag = (ClimaClosetBottom)v.getTag();
            try{
                if (bottomTag != null)
                {
                    switch (bottomTag.getAvailability()){
                        case (ClimaUtilities.AVAILABLE_TAG):
                            bottomTag.updateAvailability(ClimaUtilities.NOT_AVAILABLE_TAG);
                            break;
                        case (ClimaUtilities.NOT_AVAILABLE_TAG):
                            bottomTag.updateAvailability(ClimaUtilities.AVAILABLE_TAG);
                            break;
                    }
                }
                DB.markBottomItemDirty(bottomTag, DB.BOTTOMS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item updated!");
                if (ClimaUtilities.ASSERTIONS_ENABLED) {
                    assert markDirtyButton.getVisibility() == View.INVISIBLE : "MarkDirtyBottomButtonListener -- Mark item dirty button invisible";
                    assert deleteBottomButton.getVisibility() == View.INVISIBLE : "MarkDirtyBottomButtonListener -- delete item button invisible";
                }
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
            ClimaClosetBottom bottomTag = (ClimaClosetBottom) v.getTag();
            try {
                DB.deleteItem(bottomTag, DB.BOTTOMS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item deleted successfully");
                if (ClimaUtilities.ASSERTIONS_ENABLED) {
                    assert markDirtyButton.getVisibility() == View.INVISIBLE : "DeleteBottomButtonListener -- Mark item dirty button invisible";
                    assert deleteBottomButton.getVisibility() == View.INVISIBLE : "DeleteBottomButtonListener -- delete item button invisible";
                }
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
