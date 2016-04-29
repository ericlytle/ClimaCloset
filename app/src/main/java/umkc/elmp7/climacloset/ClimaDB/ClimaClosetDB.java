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
import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.AddItemException;
import umkc.elmp7.climacloset.Exceptions.AvailabilityException;
import umkc.elmp7.climacloset.Exceptions.DeleteItemException;
import umkc.elmp7.climacloset.Exceptions.QueryException;
import umkc.elmp7.climacloset.Exceptions.UpdateException;
import umkc.elmp7.climacloset.R;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetDB extends SQLiteOpenHelper {
    //private Context dbContext;
    private static final String IDTEXT = "id";
    private static String DATABASE_NAME = "ClimaClosetDB.sqlite";
    private static ClimaClosetDB instance;


   // public SQLiteDatabase ClimaDB;

    private ClimaClosetDB(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public static ClimaClosetDB instance(Context context){
        if (instance == null)
            instance = new ClimaClosetDB(context);
        return instance;
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

    //    Throws - DeleteItemException if an item was not delted successfully
    public boolean deleteItem(ClothingItem item, String table) throws DeleteItemException {
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        if (SQLDB.delete(table, IDTEXT + "=" + item.getID(), null) > 0)
            return true;
        else {
            DeleteItemException exception = new DeleteItemException(item, table);
            throw exception;
        }
    }

    //   Throws - AddItemException if an item was not successfully
    //            added to the database
    public boolean addTop(ClimaClosetTop newTop) throws AddItemException{
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

        rowID = SQLDB.insert(SHIRTS_TABLE, null, topValues);
        if (rowID < 0)
        {
            AddItemException exception = new AddItemException(newTop, SHIRTS_TABLE);
            throw exception;
        }

        return true;
    }

    //   Throws - AvailabilityException if an invalid availability is queried upon
    //          - QueryException if the query returns zero results
    public Cursor ClimaQueryTop(double currentTemp, String availability) throws AvailabilityException, QueryException{
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        String whereClause = "";
        switch (availability) {
            case ("All"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET) {
                    whereClause = currentTemp + "<" + SHIRTS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + SHIRTS_KEY_MIN_TEMP;
                }
                break;
            case ("Clean"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET) {
                    whereClause = currentTemp + "<" + SHIRTS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + SHIRTS_KEY_MIN_TEMP
                            + " and ";
                }
                whereClause += "'Avail' = " + SHIRTS_KEY_AVAILABLE;
                break;
            case ("Dirty"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET){
                    whereClause = currentTemp + "<" + SHIRTS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + SHIRTS_KEY_MIN_TEMP
                            + " and ";
                }
                whereClause += "'nAvail' = " + SHIRTS_KEY_AVAILABLE;
                break;
            default:
                AvailabilityException exception = new AvailabilityException(availability);
                throw exception;
        }
        Cursor cursor = SQLDB.query(SHIRTS_TABLE,
                null,
                whereClause,
                null,
                null,
                null,
                null);
        if (cursor == null){
            QueryException exception = new QueryException(BOTTOMS_TABLE, whereClause);
            throw exception;
        }

        return cursor;
    }

    //   Throws - UpdateException if the item was not updated successfully
    public boolean markBottomItemDirty(ClimaClosetBottom climaBottom) throws UpdateException
    {
        long updates;
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        ContentValues bottomValues = new ContentValues();
        switch (climaBottom.getAvailability()){
            case (ClimaUtilities.AVAILABLE_TAG):
                climaBottom.updateAvailability(ClimaUtilities.NOT_AVAILABLE_TAG);
                break;
            case (ClimaUtilities.NOT_AVAILABLE_TAG):
                climaBottom.updateAvailability(ClimaUtilities.AVAILABLE_TAG);
                break;
        }
        bottomValues.put(BOTTOMS_KEY_TYPE, climaBottom.getBottomType());
        bottomValues.put(BOTTOMS_KEY_PICTURE, getBytes(climaBottom.getPicture()));
        bottomValues.put(BOTTOMS_KEY_AVAILABILITY, climaBottom.getAvailability());
        bottomValues.put(BOTTOMS_KEY_COLOR, climaBottom.getColor());
        bottomValues.put(BOTTOMS_KEY_MIN_TEMP, climaBottom.getMinTemp());
        bottomValues.put(BOTTOMS_KEY_MAX_TEMP, climaBottom.getMaxTemp());
        updates = SQLDB.update(BOTTOMS_TABLE,
                bottomValues,
                ClimaClosetDB.BOTTOMS_KEY_ID + " = " + String.valueOf(climaBottom.getID()), null);
        if (updates < 1){
            UpdateException exception = new UpdateException(climaBottom.getID(), BOTTOMS_TABLE);
            throw exception;
        }
        return true;
    }

    //   Throws - UpdateException if the item was not updated successfully
    public boolean markTopItemDirty(ClimaClosetTop climaTop) throws UpdateException
    {
        long updates;
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        ContentValues topValues = new ContentValues();
        switch (climaTop.getAvailability()){
            case (ClimaUtilities.AVAILABLE_TAG):
                climaTop.updateAvailability(ClimaUtilities.NOT_AVAILABLE_TAG);
                break;
            case (ClimaUtilities.NOT_AVAILABLE_TAG):
                climaTop.updateAvailability(ClimaUtilities.AVAILABLE_TAG);
                break;
        }
        topValues.put(SHIRTS_KEY_SLEEVE_TYPE, climaTop.getSleeveType());
        topValues.put(SHIRTS_KEY_TOP_TYPE, climaTop.getTopType());
        topValues.put(SHIRTS_KEY_PICTURE, getBytes(climaTop.getPicture()));
        topValues.put(SHIRTS_KEY_AVAILABLE, climaTop.getAvailability());
        topValues.put(SHIRTS_KEY_COLOR, climaTop.getColor());
        topValues.put(SHIRTS_KEY_MIN_TEMP, climaTop.getMinTemp());
        topValues.put(SHIRTS_KEY_MAX_TEMP, climaTop.getMaxTemp());
        updates = SQLDB.update(SHIRTS_TABLE,
                topValues,
                ClimaClosetDB.SHIRTS_KEY_ID + " = " + String.valueOf(climaTop.getID()), null);
        if (updates < 1){
            UpdateException exception = new UpdateException(climaTop.getID(), BOTTOMS_TABLE);
            throw exception;
        }
        return true;
    }

    //   Throws - AvailabilityException if an invalid availability is queried upon
    //          - QueryException if the query returns zero results
    public Cursor ClimaQueryBottom(double currentTemp, String availability) throws QueryException, AvailabilityException{
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        String where = "";
        switch (availability){
            case ("All"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET) {
                    where = currentTemp + "<" + "b." + BOTTOMS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + "b." + BOTTOMS_KEY_MIN_TEMP;
                }
                break;
            case ("Clean"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET) {
                    where = currentTemp + "<" + "b." + BOTTOMS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + "b." + BOTTOMS_KEY_MIN_TEMP
                            + " and ";
                }
                where += "'Avail' = " + "b." + BOTTOMS_KEY_AVAILABILITY;
                break;
            case ("Dirty"):
                if (currentTemp != ClimaUtilities.NO_TEMP_SET) {
                    where = currentTemp + "<" + "b." + BOTTOMS_KEY_MAX_TEMP + "  and " + currentTemp + ">" + "b." + BOTTOMS_KEY_MIN_TEMP
                            + " and 'nAvail' = " + "b." + BOTTOMS_KEY_AVAILABILITY;
                }
                where += "'nAvail' = " + "b." + BOTTOMS_KEY_AVAILABILITY;
                break;
            default:
                AvailabilityException exception = new AvailabilityException(availability);
                throw exception;
        }
        Cursor cursor = SQLDB.query(BOTTOMS_TABLE + " b ",
                null,
                where ,//where
                null,
                null,
                null,
                null);
        if (cursor == null){
            QueryException exception = new QueryException(BOTTOMS_TABLE, where);
            throw exception;
        }
        return cursor;
    }

    //   Throws - AddItemException if an item was not successfully
    //            added to the database
    public boolean addBottom(ClimaClosetBottom newBottom) throws AddItemException{
        SQLiteDatabase SQLDB = this.getWritableDatabase();
        long rowID;

        ContentValues bottomValues = new ContentValues();
        bottomValues.put(BOTTOMS_KEY_TYPE, newBottom.getBottomType());
        bottomValues.put(BOTTOMS_KEY_PICTURE, getBytes(newBottom.getPicture()));
        bottomValues.put(BOTTOMS_KEY_AVAILABILITY, newBottom.getAvailability());
        bottomValues.put(BOTTOMS_KEY_COLOR, newBottom.getColor());
        bottomValues.put(BOTTOMS_KEY_MIN_TEMP, newBottom.getMinTemp());
        bottomValues.put(BOTTOMS_KEY_MAX_TEMP, newBottom.getMaxTemp());
        rowID = SQLDB.insert(BOTTOMS_TABLE, null, bottomValues);
        if (rowID < 0)
        {
            AddItemException exception = new AddItemException(newBottom, BOTTOMS_TABLE);
            throw exception;
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
