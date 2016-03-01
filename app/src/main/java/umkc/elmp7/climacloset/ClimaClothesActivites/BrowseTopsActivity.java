package umkc.elmp7.climacloset.ClimaClothesActivites;

import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.*;
import umkc.elmp7.climacloset.R;
public class BrowseTopsActivity extends AppCompatActivity {
    private ClimaUtilities utility;
    private ClimaClosetDB DB;
    Cursor cursor;
    ImageView imageView;
    int count = 0;
    public void onCreate(Bundle savedInstanceState) {
        ClimaClosetTop tempTop;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_tops);
        DB = new ClimaClosetDB(getApplicationContext());
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.a);
        cursor = DB.ClimaQueryTop(0.0);
        while(cursor.moveToNext()){
            //ClimaClosetTop(Bitmap picture, String availability, String color, String topType, double minTemp, double maxTemp, String sleeveType){
            tempTop = new ClimaClosetTop(utility.getCursorImage(cursor, DB.SHIRTS_KEY_PICTURE),
                    utility.getCursorString(cursor, DB.SHIRTS_KEY_AVAILABLE),
                    utility.getCursorString(cursor, DB.SHIRTS_KEY_COLOR),
                    utility.getCursorString(cursor, DB.SHIRTS_KEY_TOP_TYPE),
                    utility.getCursorDouble(cursor, DB.SHIRTS_KEY_MIN_TEMP),
                    utility.getCursorDouble(cursor, DB.SHIRTS_KEY_MAX_TEMP),
                    utility.getCursorString(cursor, DB.SHIRTS_KEY_SLEEVE_TYPE));
            imageView = new ImageView(this);
            imageView.setImageBitmap(tempTop.getPicture());
            imageView.setMinimumHeight(500);
            imageView.setMinimumWidth(500);
            imageView.setTag(tempTop);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClimaClosetTop temp = (ClimaClosetTop) v.getTag();
                    String test;
                    test = temp.getTopType();
                    Snackbar.make(findViewById(android.R.id.content), test, Snackbar.LENGTH_LONG)
                            .show();
                }
            });
            linearLayout.addView(this.imageView);
            ++count;
        }
    }

}
