package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.res.Resources;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.*;
import umkc.elmp7.climacloset.R;
public class BrowseTopsActivity extends AppCompatActivity {
    private final String WHITESPACE = " ";
    private ClimaClosetDB DB;
    private TextView topTypeTextView, sleeveTypeTextView, colorTextView, minTempTextView, maxTempTextView;
    private Cursor cursor;
    private ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        ClimaClosetTop tempTop;
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

        //Grab various element handles
        topTypeTextView = (TextView) findViewById(R.id.topTypeTextView);
        sleeveTypeTextView = (TextView) findViewById(R.id.sleeveTypeTextView);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        minTempTextView  = (TextView) findViewById(R.id.minTempTextView);
        maxTempTextView = (TextView) findViewById(R.id.maxTempTextView);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.browseTopsLayout);

        //Initialize database
        DB = new ClimaClosetDB(getApplicationContext());

        //Run query on database
        cursor = DB.ClimaQueryTop(0.0);
        while(cursor.moveToNext()){
            tempTop = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, DB.SHIRTS_KEY_PICTURE),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_AVAILABLE),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_COLOR),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_TOP_TYPE),
                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MIN_TEMP),
                    ClimaUtilities.getCursorDouble(cursor, DB.SHIRTS_KEY_MAX_TEMP),
                    ClimaUtilities.getCursorString(cursor, DB.SHIRTS_KEY_SLEEVE_TYPE),
                    ClimaUtilities.getCursorLong(cursor, DB.SHIRTS_KEY_ID));
            imageView = new ImageView(this);
            imageView.setImageBitmap(tempTop.getPicture());
            imageView.setMinimumHeight(getResources().getInteger(R.integer.image_height));
            imageView.setMinimumWidth(getResources().getInteger(R.integer.image_width));
            imageView.setTag(tempTop);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClimaClosetTop temp = (ClimaClosetTop) v.getTag();
                    topTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_topTypeDisplay) + WHITESPACE + temp.getTopType());
                    sleeveTypeTextView.setText(getResources().getString(R.string.BROWSE_TOPS_sleeveTypeDisplay) + WHITESPACE + temp.getSleeveType());
                    colorTextView.setText(getResources().getString(R.string.BROWSE_TOPS_colorDisplay) + WHITESPACE + temp.getColor());
                    minTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
                    maxTempTextView.setText(getResources().getString(R.string.BROWSE_TOPS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(temp.getID()), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
            linearLayout.addView(imageView);
        }
    }

}