package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import umkc.elmp7.climacloset.ClimaUtil.DetailsLoader;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Listeners.BackButtonClickListener;
import umkc.elmp7.climacloset.Listeners.DeleteItemButtonClickListener;
import umkc.elmp7.climacloset.Listeners.FilterSpinnerItemSelectedListener;
import umkc.elmp7.climacloset.Listeners.MarkItemDirtyButtonClickListener;
import umkc.elmp7.climacloset.Listeners.SmsSendButtonClickListener;
import umkc.elmp7.climacloset.R;
public class BrowseTopsActivity extends AppCompatActivity implements Observer {
    private ClimaClosetDB climaClosetDB;
    private Button btnDeleteTop, btnMarkItemDirty, btnSendSms;
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
        btnDeleteTop = (Button) findViewById(R.id.deleteTopButton);
        btnMarkItemDirty = (Button) findViewById(R.id.topMarkDirtyButton);
        btnSendSms = (Button) findViewById(R.id.topSendSmsButton);
        btnDeleteTop.setVisibility(View.INVISIBLE);
        btnMarkItemDirty.setVisibility(View.INVISIBLE);
        btnSendSms.setVisibility(View.INVISIBLE);

        //initialize linear layout
        linearLayout = (LinearLayout) findViewById(R.id.browseTopsLayout);

        //initialize spinner
        filterSpinner = (Spinner) findViewById(R.id.filterClothingTopSpinner);
        ClimaUtilities.buildSpinner(filterSpinner, getApplicationContext());
        filterSpinner.setSelection(0);

        //Initialize database
        climaClosetDB = ClimaClosetDB.instance(getApplicationContext());

        //Load the activity data from Database
        loadPictures(filterSpinner.getSelectedItem().toString());

        //Create Listeners
        MarkItemDirtyButtonClickListener itemDirtyButtonListener = new MarkItemDirtyButtonClickListener(getApplicationContext(), ClimaClosetDB.SHIRTS_TABLE);
        DeleteItemButtonClickListener deleteItemButtonClickListener = new DeleteItemButtonClickListener(getApplicationContext(), ClimaClosetDB.SHIRTS_TABLE, findViewById(android.R.id.content), btnMarkItemDirty, btnDeleteTop);
        FilterSpinnerItemSelectedListener filterSpinnerItemSelectedListener = new FilterSpinnerItemSelectedListener();

        //Initialize Observer's
        itemDirtyButtonListener.addObserver(this);
        deleteItemButtonClickListener.addObserver(this);
        filterSpinnerItemSelectedListener.addObserver(this);

        //Apply button listeners
        toolbar.setNavigationOnClickListener(new BackButtonClickListener(this));
        btnDeleteTop.setOnClickListener(deleteItemButtonClickListener);
        btnMarkItemDirty.setOnClickListener(itemDirtyButtonListener);
        filterSpinner.setOnItemSelectedListener(filterSpinnerItemSelectedListener);
        btnSendSms.setOnClickListener( new SmsSendButtonClickListener(this));
    }

    private void loadPictures(String availability){
        DetailsLoader detailsLoader;
        Cursor cursor;
        //Run query on database
        try {
            if (!ClimaUtilities.temperature.equals("NOT SET")) {
                cursor = climaClosetDB.ClimaQueryTop(Double.parseDouble(ClimaUtilities.temperature), availability);
            }
            else
            {
                cursor = climaClosetDB.ClimaQueryTop(ClimaUtilities.NO_TEMP_SET, availability);
            }
            detailsLoader = new DetailsLoader(cursor, linearLayout, btnDeleteTop, btnMarkItemDirty, btnSendSms, textViewMap, this);
            detailsLoader.LoadPictures();
        }
        catch(AvailabilityException e){
            Log.d("BrowseTops", e.getMessage());
        }
        catch(QueryException e){
            Log.d("BrowseTops", e.getMessage());
        }
    }

    private void clearFields(){
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_TOP_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_SLEEVE_TYPE).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_COLOR).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_MIN_TEMP).setText("");
        textViewMap.get(ClimaClosetDB.SHIRTS_KEY_MAX_TEMP).setText("");
        btnDeleteTop.setVisibility(View.INVISIBLE);
        btnMarkItemDirty.setVisibility(View.INVISIBLE);
        btnSendSms.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update(Observable observable, Object o) {
        loadPictures(filterSpinner.getSelectedItem().toString());
        clearFields();
        ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), (String) o);
    }

}
