package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.ClimaUtil.DetailsLoader;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Listeners.BackButtonClickListener;
import umkc.elmp7.climacloset.Listeners.DeleteItemButtonClickListener;
import umkc.elmp7.climacloset.Listeners.FilterSpinnerItemSelectedListener;
import umkc.elmp7.climacloset.Listeners.MarkItemDirtyButtonClickListener;
import umkc.elmp7.climacloset.Listeners.SmsSendButtonClickListener;
import umkc.elmp7.climacloset.R;

public class BrowseBottomsActivity extends AppCompatActivity implements Observer {
    private ClimaClosetDB climaClosetDB;
    private Button btnDeleteBottom, btnMarkDirty, btnSendSms;
    private Map<String, TextView> textViewMap;
    private Cursor cursor;
    private LinearLayout linearLayout;
    private Spinner filterSpinner;

    public void onCreate(Bundle savedInstanceState) {
        textViewMap = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_bottoms);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        btnDeleteBottom = (Button) findViewById(R.id.deleteBottomButton);
        btnMarkDirty = (Button) findViewById(R.id.markDirtyBottomButton);
        btnSendSms = (Button) findViewById(R.id.bottomSendSmsButton);
        btnDeleteBottom.setVisibility(View.INVISIBLE);
        btnMarkDirty.setVisibility(View.INVISIBLE);
        btnSendSms.setVisibility(View.INVISIBLE);

        //Initialize linear layout
        linearLayout = (LinearLayout) findViewById(R.id.browseBottomLayout);

        //Ititialize spinner
        filterSpinner = (Spinner) findViewById(R.id.filterClothingSpinner);
        ClimaUtilities.buildSpinner(filterSpinner, getApplicationContext());
        filterSpinner.setSelection(0);

        //Initialize Database
        climaClosetDB = ClimaClosetDB.instance(getApplicationContext());

        //Load pictures corresponding to item selected in the spinner(All)
        loadPictures(filterSpinner.getSelectedItem().toString());

        //Create Listeners
        MarkItemDirtyButtonClickListener itemDirtyButtonListener = new MarkItemDirtyButtonClickListener(getApplicationContext(), ClimaClosetDB.BOTTOMS_TABLE);
        DeleteItemButtonClickListener deleteItemButtonClickListener = new DeleteItemButtonClickListener(getApplicationContext(), ClimaClosetDB.BOTTOMS_TABLE, findViewById(android.R.id.content), btnMarkDirty, btnDeleteBottom);
        FilterSpinnerItemSelectedListener filterSpinnerItemSelectedListener = new FilterSpinnerItemSelectedListener();

        //Initialize observers
        itemDirtyButtonListener.addObserver(this);
        deleteItemButtonClickListener.addObserver(this);
        filterSpinnerItemSelectedListener.addObserver(this);

        //Initialize button click listeners
        toolbar.setNavigationOnClickListener(new BackButtonClickListener(this));
        btnDeleteBottom.setOnClickListener(deleteItemButtonClickListener);
        btnMarkDirty.setOnClickListener(itemDirtyButtonListener);
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemSelectedListener);
        btnSendSms.setOnClickListener(new SmsSendButtonClickListener(this));
    }

    private void loadPictures(String availability){
        DetailsLoader detailsLoader;

        try {
            if (!ClimaUtilities.temperature.equals("NOT SET")) {
                cursor = climaClosetDB.ClimaQueryBottom(Double.parseDouble(ClimaUtilities.temperature), availability);
            }
            else{
                cursor = climaClosetDB.ClimaQueryBottom(ClimaUtilities.NO_TEMP_SET, availability);
            }
            detailsLoader = new DetailsLoader(cursor, linearLayout, btnDeleteBottom, btnMarkDirty, btnSendSms, textViewMap, this);
            detailsLoader.LoadPictures();
        }
        catch (QueryException e){
            Log.d("BrowseBottoms", e.getMessage());
        }
        catch (AvailabilityException e){
            Log.d("BrowseBottoms", e.getMessage());
        }
    }

    private void clearFields(){
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_COLOR).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP).setText("");
        textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP).setText("");
        btnDeleteBottom.setVisibility(View.INVISIBLE);
        btnMarkDirty.setVisibility(View.INVISIBLE);
        btnSendSms.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update(Observable observable, Object o) {
        loadPictures(filterSpinner.getSelectedItem().toString());
        clearFields();
        ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), (String) o);
    }
}
