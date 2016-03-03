package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.R;

public class BrowseBottomsActivity extends AppCompatActivity {
    private final String WHITESPACE = " ";
    private ClimaClosetDB DB;
    private TextView bottomTypeTextView, colorTextView, minTempTextView, maxTempTextView;
    private Cursor cursor;
    private ImageView imageView;
    public void onCreate(Bundle savedInstanceState) {
        ClimaClosetBottom tempBottom;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_bottoms);

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

        bottomTypeTextView = (TextView) findViewById(R.id.bottomTypeTextView);
        colorTextView = (TextView) findViewById(R.id.colorBottomTextView);
        minTempTextView  = (TextView) findViewById(R.id.minTempBottomTextView);
        maxTempTextView = (TextView) findViewById(R.id.maxTempBottomTextView);
        DB = new ClimaClosetDB(getApplicationContext());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.browseBottomLayout);
        cursor = DB.ClimaQueryBottom(0.0);
        while(cursor.moveToNext()){
            //ClimaClosetTop(Bitmap picture, String availability, String color, String topType, double minTemp, double maxTemp, String sleeveType){
            tempBottom = new ClimaClosetBottom(ClimaUtilities.getCursorImage(cursor, DB.BOTTOMS_KEY_PICTURE),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_AVAILABILITY),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_COLOR),
                    ClimaUtilities.getCursorString(cursor, DB.BOTTOMS_KEY_TYPE),
                    ClimaUtilities.getCursorDouble(cursor, DB.BOTTOMS_KEY_MIN_TEMP),
                    ClimaUtilities.getCursorDouble(cursor, DB.BOTTOMS_KEY_MAX_TEMP),
                    ClimaUtilities.getCursorLong(cursor, DB.BOTTOMS_KEY_ID));
            imageView = new ImageView(this);
            imageView.setImageBitmap(tempBottom.getPicture());
            imageView.setMinimumHeight(getResources().getInteger(R.integer.image_height));
            imageView.setMinimumWidth(getResources().getInteger(R.integer.image_width));
            imageView.setTag(tempBottom);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClimaClosetBottom temp = (ClimaClosetBottom) v.getTag();
                    bottomTypeTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_bottomTypeDisplay) + WHITESPACE + temp.getBottomType());
                    colorTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_colorDisplay) + WHITESPACE + temp.getColor());
                    minTempTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
                    maxTempTextView.setText(getResources().getString(R.string.BROWSE_BOTTOMS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(temp.getID()), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
            linearLayout.addView(this.imageView);
        }
    }

}
