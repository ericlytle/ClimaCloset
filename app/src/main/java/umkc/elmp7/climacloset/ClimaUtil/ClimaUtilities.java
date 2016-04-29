package umkc.elmp7.climacloset.ClimaUtil;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.R;

/**
 * Created by ericl on 2/29/2016.
 */
public class ClimaUtilities {
    public static boolean ASSERTIONS_ENABLED  = true;
    public static final String AVAILABLE_TAG = "Avail";
    public static final String NOT_AVAILABLE_TAG = "nAvail";
    public static String temperature = "NOT SET";
    public static final double NO_TEMP_SET = -5000;
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String getCursorString(Cursor tCursor, String columnIdx){
        return tCursor.getString(tCursor.getColumnIndex(columnIdx));
    }

    public static Bitmap getCursorImage(Cursor tCursor, String columnIdx){
        return getImage(tCursor.getBlob(tCursor.getColumnIndex(columnIdx)));
    }

    public static double getCursorDouble(Cursor tCursor, String columnIdx){
        return tCursor.getDouble(tCursor.getColumnIndex(columnIdx));
    }

    public static long getCursorLong(Cursor tCursor, String columnIdx){
        return tCursor.getLong(tCursor.getColumnIndex(columnIdx));
    }

    public static String parseSpaces(String parseString){
        return parseString.replaceAll(" ", "%20");
    }

    public static void SnackbarMessage(View view, String message)
    {
        Snackbar.make(view,
                message,
                Snackbar.LENGTH_LONG)
                .show();
    }

    public static void buildSpinner(Spinner spinner, Context context){
        String[] filterItemsArray = context.getResources().getStringArray(R.array.clothingFilter);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, filterItemsArray )
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
        spinner.setAdapter(spinnerAdapter);
    }
    public static void AlertMessage(Activity activity, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Okay", null);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
