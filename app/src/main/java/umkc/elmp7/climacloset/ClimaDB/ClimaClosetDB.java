package umkc.elmp7.climacloset.ClimaDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetDB extends SQLiteOpenHelper {
    //private Context dbContext;
    private static String DATABASE_NAME = "ClimaClosetDB.sqlite";


    public SQLiteDatabase ClimaDB;

    public ClimaClosetDB(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase SQLDB){
        String CREATE_SHIRTS_TABLE  = "CREATE TABLE " + SHIRTS_TABLE + "(" + SHIRTS_KEY_ID
                + " INTEGER PRIMARY KEY," + SHIRTS_KEY_SLEEVE_TYPE + " TEXT," + SHIRTS_KEY_TOP_TYPE
                + " TEXT," + SHIRTS_KEY_PICTURE + " BLOB," + SHIRTS_KEY_AVAILABLE + " TEXT,"
                + SHIRTS_KEY_COLOR + " TEXT," + SHIRTS_KEY_MIN_TEMP + " REAL,"
                + SHIRTS_KEY_MAX_TEMP + " REAL" + ")";
        SQLDB.execSQL(CREATE_SHIRTS_TABLE);

        String CREATE_BOTTOMS_TABLE  = "CREATE TABLE " + BOTTOMS_TABLE + "(" + BOTTOMS_KEY_ID
                + " INTEGER PRIMARY KEY," + BOTTOMS_KEY_TYPE + " TEXT," + BOTTOMS_KEY_AVAILABILITY
                + " TEXT," + BOTTOMS_KEY_PICTURE + " BLOB," + BOTTOMS_KEY_COLOR + " TEXT,"
                + BOTTOMS_KEY_MIN_TEMP + " REAL," + BOTTOMS_KEY_MAX_TEMP + " REAL" + ")";
        SQLDB.execSQL(CREATE_BOTTOMS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase SQLDB, int previousVersion, int newVersion){
        SQLDB.execSQL("DROP TABLE IF EXISTS " + SHIRTS_TABLE);
        onCreate(SQLDB);
    }

    public boolean addTop(ClimaClosetTop newTop) throws SQLException{
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        long rowID;

        ContentValues topValues = new ContentValues();
        topValues.put(SHIRTS_KEY_SLEEVE_TYPE, newTop.getSleeveType());
        topValues.put(SHIRTS_KEY_TOP_TYPE, newTop.getTopType());
        topValues.put(SHIRTS_KEY_PICTURE, getBytes(newTop.getPicture()));
        topValues.put(SHIRTS_KEY_AVAILABLE, newTop.getAvailability());
        topValues.put(SHIRTS_KEY_COLOR, newTop.getColor());
        topValues.put(SHIRTS_KEY_MIN_TEMP, newTop.getMinTemp());
        topValues.put(SHIRTS_KEY_MAX_TEMP, newTop.getMaxTemp());

        try{
            rowID = SQLDB.insertOrThrow(SHIRTS_TABLE, null, topValues);
        }
        catch(Exception exception)
        {
            Log.e("Exception", "SQLInsert::addTop::" + String.valueOf(exception.getMessage()));
            return false;
        }
        return true;
    }
    public Cursor ClimaQueryTop(){
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        String[] from = { SHIRTS_KEY_ID,
                SHIRTS_KEY_TOP_TYPE };
        Cursor cursor = SQLDB.query(SHIRTS_TABLE,
                null,
                SHIRTS_KEY_TOP_TYPE + "= '" + "test" + "'",
                null,
                null,
                null,
                null);
        return cursor;
    }
    public boolean addBottom(ClimaClosetBottom newBottom) throws SQLException{
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        long rowID;

        ContentValues bottomValues = new ContentValues();
        bottomValues.put(BOTTOMS_KEY_TYPE, newBottom.getBottomType());
        //bottomValues.put(BOTTOMS_KEY_AVAILABILITY, newBottom.getPicture());
        bottomValues.put(BOTTOMS_KEY_PICTURE, newBottom.getAvailability());
        bottomValues.put(BOTTOMS_KEY_COLOR, newBottom.getColor());
        bottomValues.put(BOTTOMS_KEY_MIN_TEMP, newBottom.getMinTemp());
        bottomValues.put(BOTTOMS_KEY_MAX_TEMP, newBottom.getMaxTemp());

        try{
            rowID = SQLDB.insertOrThrow(BOTTOMS_TABLE, null, bottomValues);
        }
        catch(Exception exception)
        {
            Log.e("Exception", "SQLInsert::addBottom::" + String.valueOf(exception.getMessage()));
            return false;
        }
        return true;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    //SHIRTS TABLE
    public static String SHIRTS_TABLE = "tops";
    public static String SHIRTS_KEY_ID = "id"; //integer
    public static String SHIRTS_KEY_SLEEVE_TYPE = "sleeve_type"; //string
    public static String SHIRTS_KEY_TOP_TYPE = "top_type"; //string
    public static String SHIRTS_KEY_PICTURE = "picture"; //blob
    public static String SHIRTS_KEY_AVAILABLE = "available"; //boolean:clean=true/dirty=false
    public static String SHIRTS_KEY_COLOR = "color"; //string
    public static String SHIRTS_KEY_MIN_TEMP = "min_temp"; //double
    public static String SHIRTS_KEY_MAX_TEMP = "max_temp"; //double

    //BOTTOMS TABLE
    public static String BOTTOMS_TABLE = "bottoms";
    public static String BOTTOMS_KEY_ID = "id";
    public static String BOTTOMS_KEY_TYPE = "bottoms_type"; //string
    public static String BOTTOMS_KEY_AVAILABILITY = "available"; //boolean:clean=true/dirty=false
    public static String BOTTOMS_KEY_PICTURE = "picture"; //blob
    public static String BOTTOMS_KEY_COLOR = "color"; //string
    public static String BOTTOMS_KEY_MIN_TEMP = "min_temp"; //double
    public static String BOTTOMS_KEY_MAX_TEMP = "max_temp"; //double
}
