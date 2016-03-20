package umkc.elmp7.climacloset.ClimaUtil;

import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by ericl on 2/29/2016.
 */
public class ClimaUtilities {
    public static String temperature;
    private static Resources resources;
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

    public static String getResourceString(int ID){
        return resources.getString(ID);
    }

    public static String parseSpaces(String parseString){
        return parseString.replaceAll(" ", "%20");
    }
}
