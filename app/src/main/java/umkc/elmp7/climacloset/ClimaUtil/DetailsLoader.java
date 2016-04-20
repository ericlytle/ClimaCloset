package umkc.elmp7.climacloset.ClimaUtil;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.R;

/**
 * Created by ericl on 4/19/2016.
 */
public class DetailsLoader {
    private Cursor cursor;
    private LinearLayout linearLayout;
    private Button deleteButton, dirtyButton;
    private ImageView imageView;
    private Map<String, TextView> textViewMap;
    private ClothingItem clothingItem;
    private Activity activity;
    private int clothingIndicator;
    private static final int TOP_INDICATOR = 1;
    private static final int BOTTOM_INDICATOR = 2;
    private static final String WHITESPACE = " ";

    public DetailsLoader(Cursor cursor, LinearLayout linearLayout, Button deleteButton, Button dirtyButton,
                         Map textViewMap, Activity activity){
        this.cursor = cursor;
        this.linearLayout = linearLayout;
        this.deleteButton = deleteButton;
        this.dirtyButton = dirtyButton;
        this.textViewMap = textViewMap;
        this.clothingItem = clothingItem;
        this.activity = activity;
        if (textViewMap.containsKey(ClimaClosetDB.SHIRTS_KEY_ID))
        {
            clothingIndicator = TOP_INDICATOR;
        }
        else
        {
            clothingIndicator = BOTTOM_INDICATOR;
        }
    }

    public void LoadPictures(){
        ClimaClosetTop topTag;
        ClimaClosetBottom bottomTag;
        this.linearLayout.removeAllViews();
        while(this.cursor.moveToNext()){
            imageView = new ImageView(this.activity); //fix this
            imageView.setImageBitmap(ClimaUtilities.getCursorImage(cursor, ClimaClosetDB.SHIRTS_KEY_PICTURE));
            imageView.setMinimumHeight(this.activity.getResources().getInteger(R.integer.image_height));
            imageView.setMinimumWidth(this.activity.getResources().getInteger(R.integer.image_width));
            if (clothingIndicator == TOP_INDICATOR){
                topTag = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, ClimaClosetDB.SHIRTS_KEY_PICTURE),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.SHIRTS_KEY_AVAILABLE),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.SHIRTS_KEY_COLOR),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.SHIRTS_KEY_TOP_TYPE),
                        ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.SHIRTS_KEY_MIN_TEMP),
                        ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.SHIRTS_KEY_MAX_TEMP),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.SHIRTS_KEY_SLEEVE_TYPE),
                        ClimaUtilities.getCursorLong(cursor, ClimaClosetDB.SHIRTS_KEY_ID));
                imageView.setTag(topTag);

            }
            if (clothingIndicator == BOTTOM_INDICATOR){
                bottomTag = new ClimaClosetBottom(ClimaUtilities.getCursorImage(cursor, ClimaClosetDB.BOTTOMS_KEY_PICTURE),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_AVAILABILITY),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_COLOR),
                        ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_TYPE),
                        ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP),
                        ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP),
                        ClimaUtilities.getCursorLong(cursor, ClimaClosetDB.SHIRTS_KEY_ID));
                imageView.setTag(bottomTag);
                imageView.setOnClickListener(new bottomViewOnClickListener());
            }
            linearLayout.addView(imageView);
        }
    }
    private class bottomViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ClimaClosetBottom bottomTag = (ClimaClosetBottom) v.getTag();
            //bottomTypeTextView.
            textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_TYPE).setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_bottomTypeDisplay) + WHITESPACE + bottomTag.getBottomType());
            //colorTextView.setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_colorDisplay) + WHITESPACE + temp.getColor());
            textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_COLOR).setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_colorDisplay) + WHITESPACE + bottomTag.getColor());
            //minTempTextView.setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_minTempDisplay) + WHITESPACE + String.valueOf(temp.getMinTemp()));
            textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP).setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_minTempDisplay) + WHITESPACE + String.valueOf(bottomTag.getMinTemp()));
            //maxTempTextView.setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_maxTempDisplay) + WHITESPACE + String.valueOf(temp.getMaxTemp()));
            textViewMap.get(ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP).setText(activity.getResources().getString(R.string.BROWSE_BOTTOMS_maxTempDisplay) + WHITESPACE + String.valueOf(bottomTag.getMaxTemp()));
            Snackbar.make(activity.findViewById(android.R.id.content), activity.getResources().getString(R.string.SNACKBAR_id_display) + WHITESPACE + String.valueOf(bottomTag.getID()), Snackbar.LENGTH_LONG)
                    .show();
            if (bottomTag.getAvailability().equalsIgnoreCase("Avail")) {
                dirtyButton.setText("Mark Dirty");
            }
            if (bottomTag.getAvailability().equalsIgnoreCase("nAvail")) {
                dirtyButton.setText("Mark Clean");
            }
            deleteButton.setTag(bottomTag);//important to be able to get later
            dirtyButton.setTag(bottomTag);
            deleteButton.setVisibility(View.VISIBLE);
            dirtyButton.setVisibility(View.VISIBLE);
        }
    }
}


