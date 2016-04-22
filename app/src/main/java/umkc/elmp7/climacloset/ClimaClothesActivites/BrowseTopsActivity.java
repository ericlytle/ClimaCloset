package umkc.elmp7.climacloset.ClimaClothesActivites;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.*;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.DeleteItemException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Exceptions.UpdateException;
import umkc.elmp7.climacloset.R;
public class BrowseTopsActivity extends AppCompatActivity {
    private final String WHITESPACE = " ";
    private ClimaClosetDB DB;
    private TextView topTypeTextView, sleeveTypeTextView, colorTextView, minTempTextView, maxTempTextView;
    private Cursor cursor;
    private ImageView imageView;
    private Button deleteTopButton, markItemDirtyButton;
    private ClimaClosetTop tempTop,temp;
    private LinearLayout linearLayout;
    private Spinner filterSpinner;
    private Map<String, TextView> textViewMap;

    public void onCreate(Bundle savedInstanceState) {
        textViewMap = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_tops);
        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(backButtonClickListener);
        getSupportActionBar().setIcon(R.drawable.climatoolbarsmall);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize textviews
        textViewMap.put(ClimaClosetDB.SHIRTS_KEY_TOP_TYPE, (TextView) findViewById(R.id.topTypeTextView));
        textViewMap.put(ClimaClosetDB.SHIRTS_KEY_SLEEVE_TYPE, (TextView) findViewById(R.id.sleeveTypeTextView));
        textViewMap.put(ClimaClosetDB.SHIRTS_KEY_COLOR, (TextView) findViewById(R.id.colorTextView));
        textViewMap.put(ClimaClosetDB.SHIRTS_KEY_MIN_TEMP, (TextView) findViewById(R.id.minTempTextView));
        textViewMap.put(ClimaClosetDB.SHIRTS_KEY_MAX_TEMP, (TextView) findViewById(R.id.maxTempTextView));

        //initialize buttons
        deleteTopButton = (Button) findViewById(R.id.deleteTopButton);
        markItemDirtyButton = (Button) findViewById(R.id.topMarkDirtyButton);
        deleteTopButton.setVisibility(View.INVISIBLE);
        markItemDirtyButton.setVisibility(View.INVISIBLE);

        //initialize linear layout
        linearLayout = (LinearLayout) findViewById(R.id.browseTopsLayout);

        //initialize spinner
        filterSpinner = (Spinner) findViewById(R.id.filterClothingTopSpinner);
        //buildSpinner();
        ClimaUtilities.buildSpinner(filterSpinner, getApplicationContext());
        filterSpinner.setSelection(0);

        //Initialize database
        DB = ClimaClosetDB.instance(getApplicationContext());

        loadPictures(filterSpinner.getSelectedItem().toString());

        //initialize button listeners
        deleteTopButton.setOnClickListener(deleteTopButtonClickListener);
        markItemDirtyButton.setOnClickListener(markItemDirtyButtonClickListener);
        filterSpinner.setOnItemSelectedListener(itemSelectedListener);

    }

    private void loadPictures(String availability){
        DetailsLoader detailsLoader;
        //Run query on database
        //done in details loader? linearLayout.removeAllViews();
        try {
            cursor = DB.ClimaQueryTop(Double.parseDouble(ClimaUtilities.temperature), availability);
            detailsLoader = new DetailsLoader(cursor, linearLayout, deleteTopButton, markItemDirtyButton, textViewMap, this);
            detailsLoader.LoadPictures();
        }
        catch(AvailabilityException e){
            Log.d("BrowseTops", e.getMessage());
        }
        catch(QueryException e){
            Log.d("BrowseTops", e.getMessage());
        }
//        while(cursor.moveToNext()){
//            tempTop = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, DB.SHIRTS_KEY_PICTURE),
//                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_AVAILABLE),
//                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_COLOR),
//                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_TOP_TYPE),
//                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MIN_TEMP),
//                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MAX_TEMP),
//                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_SLEEVE_TYPE),
//                    ClimaUtilities.getCursorLong(cursor, DB.SHIRTS_KEY_ID));
//
//            imageView = new ImageView(this);
//            imageView.setImageBitmap(tempTop.getPicture());
//            imageView.setMinimumHeight(getResources().getInteger(R.integer.image_height));
//            imageView.setMinimumWidth(getResources().getInteger(R.integer.image_width));
//            imageView.setTag(tempTop);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    temp = (ClimaClosetTop) v.getTag();
//                    topTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_topTypeDisplay) + WHITESPACE + temp.getTopType());
//                    sleeveTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_sleeveTypeDisplay) + WHITESPACE + temp.getSleeveType());
//                    colorTextView.setText(getResources().getString(R.string.BROWSE_TOPS_colorDisplay) + WHITESPACE + temp.getColor());
//                    minTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
//                    maxTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
//                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(temp.getID()), Snackbar.LENGTH_LONG)
//                            .show();
//                    if (temp.getAvailability().equalsIgnoreCase("Avail")) {
//                        markItemDirtyButton.setText("Mark Dirty");
//                    }
//                    if (temp.getAvailability().equalsIgnoreCase("nAvail")) {
//                        markItemDirtyButton.setText("Mark Clean");
//                    }
//                    deleteTopButton.setVisibility(View.VISIBLE);
//                    markItemDirtyButton.setVisibility(View.VISIBLE);
//                }
//            });
//            linearLayout.addView(imageView);
//        }
        if (linearLayout.getChildCount() == 0) {
            deleteTopButton.setVisibility(View.INVISIBLE);
            markItemDirtyButton.setVisibility(View.INVISIBLE);
        }
        else {
            deleteTopButton.setVisibility(View.VISIBLE);
            markItemDirtyButton.setVisibility(View.VISIBLE);
        }
    }

    private void clearFields(){
//        topTypeTextView.setText("");
//        sleeveTypeTextView.setText("");
//        colorTextView.setText("");
//        minTempTextView.setText("");
//        maxTempTextView.setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_TOP_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_SLEEVE_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_COLOR).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_MIN_TEMP).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_MAX_TEMP).setText("");
        deleteTopButton.setVisibility(View.INVISIBLE);
        markItemDirtyButton.setVisibility(View.INVISIBLE);
    }

    private View.OnClickListener deleteTopButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ClimaClosetTop topTag = (ClimaClosetTop) v.getTag();
            try {
                DB.deleteItem(topTag, DB.SHIRTS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully", Snackbar.LENGTH_LONG)
                        .show();
                if (ClimaUtilities.ASSERTIONS_ENABLED) {
                    assert markItemDirtyButton.getVisibility() == View.INVISIBLE : "DeleteTopButtonListener -- Mark item dirty button invisible";
                    assert deleteTopButton.getVisibility() == View.INVISIBLE : "DeleteTopButtonListener -- delete item button invisible";
                }
            }
            catch (DeleteItemException e)
            {
                Log.d("DeleteItemException", e.getMessage());
            }
        }
    };

    private View.OnClickListener markItemDirtyButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            ClimaClosetTop topTag = (ClimaClosetTop) v.getTag();
            try{
                if (topTag != null)
                {
                    switch (topTag.getAvailability()){
                        case (ClimaUtilities.AVAILABLE_TAG):
                            topTag.updateAvailability(ClimaUtilities.NOT_AVAILABLE_TAG);
                            break;
                        case (ClimaUtilities.NOT_AVAILABLE_TAG):
                            topTag.updateAvailability(ClimaUtilities.AVAILABLE_TAG);
                            break;
                    }
                }
                DB.markTopItemDirty(temp, DB.SHIRTS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item updated!");
                if (ClimaUtilities.ASSERTIONS_ENABLED) {
                    assert markItemDirtyButton.getVisibility() == View.INVISIBLE : "MarkDirtyTopButtonListener -- Mark item dirty button invisible";
                    assert deleteTopButton.getVisibility() == View.INVISIBLE : "MarkDirtyTopButtonListener -- delete item button invisible";
                }
            }
            catch (UpdateException e)
            {
                ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item not updated!");
            }
        }
    };

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            clearFields();
            loadPictures(filterSpinner.getSelectedItem().toString());
            if (ClimaUtilities.ASSERTIONS_ENABLED) {
                assert markItemDirtyButton.getVisibility() == View.INVISIBLE : "ItemSelectedTopButtonListener -- Mark item dirty button invisible";
                assert deleteTopButton.getVisibility() == View.INVISIBLE : "ItemSelectedTopButtonListener -- delete item button invisible";
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

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
