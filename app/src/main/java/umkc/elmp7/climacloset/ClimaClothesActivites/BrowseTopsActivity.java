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
    private ClimaClosetTop tempTop,temp, updateAvailTop;
    private LinearLayout linearLayout;
    private Spinner filterSpinner;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_tops);

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

        //initialize textviews
        topTypeTextView = (TextView) findViewById(R.id.topTypeTextView);
        sleeveTypeTextView = (TextView) findViewById(R.id.sleeveTypeTextView);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        minTempTextView  = (TextView) findViewById(R.id.minTempTextView);
        maxTempTextView = (TextView) findViewById(R.id.maxTempTextView);

        //initialize buttons
        deleteTopButton = (Button) findViewById(R.id.deleteTopButton);
        markItemDirtyButton = (Button) findViewById(R.id.topMarkDirtyButton);
        deleteTopButton.setVisibility(View.INVISIBLE);
        markItemDirtyButton.setVisibility(View.INVISIBLE);

        //initialize linear layout
        linearLayout = (LinearLayout) findViewById(R.id.browseTopsLayout);

        //initialize spinner
        filterSpinner = (Spinner) findViewById(R.id.filterClothingTopSpinner);
        buildSpinner();

        //Initialize database
        DB = new ClimaClosetDB(getApplicationContext());

        loadPictures(filterSpinner.getSelectedItem().toString());

        //initialize button listeners
        deleteTopButton.setOnClickListener(deleteTopButtonClickListener);
        markItemDirtyButton.setOnClickListener(markItemDirtyButtonClickListener);
        filterSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void loadPictures(String availability){
        final ClimaClosetDB DB = new ClimaClosetDB(getApplicationContext());
        //Run query on database
        linearLayout.removeAllViews();
        try {
            cursor = DB.ClimaQueryTop(Double.parseDouble(ClimaUtilities.temperature), availability);
        }
        catch(AvailabilityException e){
            Log.d("BrowseTops", e.getMessage());
        }
        catch(QueryException e){
            Log.d("BrowseTops", e.getMessage());
        }
        while(cursor.moveToNext()){
            tempTop = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, DB.SHIRTS_KEY_PICTURE),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_AVAILABLE),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_COLOR),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_TOP_TYPE),
                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MIN_TEMP),
                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MAX_TEMP),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_SLEEVE_TYPE),
                    ClimaUtilities.getCursorLong(cursor, DB.SHIRTS_KEY_ID));
            if (imageView != null) {
                if (imageView.getTag() == null) {
                    updateAvailTop = tempTop;
                } else {
                    updateAvailTop = (ClimaClosetTop) imageView.getTag();
                }
            }
            imageView = new ImageView(this);
            imageView.setImageBitmap(tempTop.getPicture());
            imageView.setMinimumHeight(getResources().getInteger(R.integer.image_height));
            imageView.setMinimumWidth(getResources().getInteger(R.integer.image_width));
            imageView.setTag(tempTop);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp = (ClimaClosetTop) v.getTag();
                    topTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_topTypeDisplay) + WHITESPACE + temp.getTopType());
                    sleeveTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_sleeveTypeDisplay) + WHITESPACE + temp.getSleeveType());
                    colorTextView.setText(getResources().getString(R.string.BROWSE_TOPS_colorDisplay) + WHITESPACE + temp.getColor());
                    minTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
                    maxTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(temp.getID()), Snackbar.LENGTH_LONG)
                            .show();
                    if (temp.getAvailability().equalsIgnoreCase("Avail")) {
                        markItemDirtyButton.setText("Mark Dirty");
                    }
                    if (temp.getAvailability().equalsIgnoreCase("nAvail")) {
                        markItemDirtyButton.setText("Mark Clean");
                    }
                    deleteTopButton.setVisibility(View.VISIBLE);
                    markItemDirtyButton.setVisibility(View.VISIBLE);
                }
            });
            linearLayout.addView(imageView);
        }
//        if (linearLayout.getChildCount() == 0)
//            deleteTopButton.setVisibility(View.INVISIBLE);
//        else
//            deleteTopButton.setVisibility(View.VISIBLE);
    }

    private void clearFields(){
        topTypeTextView.setText("");
        sleeveTypeTextView.setText("");
        colorTextView.setText("");
        minTempTextView.setText("");
        maxTempTextView.setText("");
        deleteTopButton.setVisibility(View.INVISIBLE);
        markItemDirtyButton.setVisibility(View.INVISIBLE);
    }

    void buildSpinner(){
        String[] colorArray = getResources().getStringArray(R.array.clothingFilter);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colorArray )
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

    private View.OnClickListener deleteTopButtonClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            try
            {
                DB.deleteItem(temp, DB.SHIRTS_TABLE);
                loadPictures(filterSpinner.getSelectedItem().toString());
                clearFields();
                Snackbar.make(findViewById(android.R.id.content), "Item deleted successfully", Snackbar.LENGTH_LONG)
                        .show();
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
                DB.markTopItemDirty(temp, DB.BOTTOMS_TABLE);
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

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            clearFields();
            loadPictures(filterSpinner.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };
}
