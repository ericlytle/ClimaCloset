package umkc.elmp7.climacloset;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;

/**
 * Created by Eric on 4/21/16.
 */
public class ClimaDBTest extends android.test.AndroidTestCase  {
    private ClimaClosetDB ClimaDB;

    public void setUp(){
        Context c = getContext();
        ClimaDB = ClimaClosetDB.instance(c);
    }
    public void tearDown(){

    }

    public void testAddTop() throws Exception{
        boolean success;
        //create blank bitmap image
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
        success = ClimaDB.addTop(new ClimaClosetTop(bmp, ClimaUtilities.AVAILABLE_TAG, "Red", "Shirt", 33.00, 99.00, "Long"));
        assertEquals(success, true);
    }

    public void testClimaQueryTop() throws Exception{
        Cursor cursor;
        cursor = ClimaDB.ClimaQueryTop(55, "All");
        //give the query some time to process
        Thread.sleep(200);
        assertNotNull(cursor);
    }

    public void testDeleteItem() throws Exception{
        long itemID;
        boolean success;
        ClimaClosetBottom nullObject;
        Cursor cursor;
        cursor = ClimaDB.ClimaQueryTop(55, "All");
        //give the query some time to process
        Thread.sleep(500);
        cursor.moveToFirst();
        itemID = cursor.getLong(cursor.getColumnIndex(ClimaDB.SHIRTS_KEY_ID));
        nullObject = new ClimaClosetBottom(null, null, null, null, 0, 0, itemID);
        success = ClimaDB.deleteItem(nullObject, ClimaDB.SHIRTS_TABLE);
        assertEquals(success, true);
    }

    public void testMarkTopItemDirty() throws Exception{
        boolean success;
        Cursor cursor;
        ClimaClosetTop testTop;
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
        success = ClimaDB.addTop(new ClimaClosetTop(bmp, ClimaUtilities.AVAILABLE_TAG, "Red", "Shirt", 33.00, 99.00, "Long"));
        Thread.sleep(500);
        assertEquals(success, true);
        cursor = ClimaDB.ClimaQueryTop(55, "All");
        Thread.sleep(800);
        assertNotNull(cursor);
        cursor.moveToFirst();
        testTop = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, ClimaDB.SHIRTS_KEY_PICTURE),
                    ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_AVAILABLE),
                    ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_COLOR),
                    ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_TOP_TYPE),
                    ClimaUtilities.getCursorDouble(cursor, ClimaDB.SHIRTS_KEY_MIN_TEMP),
                    ClimaUtilities.getCursorDouble(cursor, ClimaDB.SHIRTS_KEY_MAX_TEMP),
                    ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_SLEEVE_TYPE),
                    ClimaUtilities.getCursorLong(cursor, ClimaDB.SHIRTS_KEY_ID));
        assertNotNull(testTop);
        assertEquals(ClimaUtilities.AVAILABLE_TAG, testTop.getAvailability());
        success = ClimaDB.markTopItemDirty(testTop);
        Thread.sleep(800);
        assertEquals(success, true);
        cursor = ClimaDB.ClimaQueryTop(55, "All");
        Thread.sleep(800);
        cursor.moveToFirst();
        testTop = new ClimaClosetTop(ClimaUtilities.getCursorImage(cursor, ClimaDB.SHIRTS_KEY_PICTURE),
                ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_AVAILABLE),
                ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_COLOR),
                ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_TOP_TYPE),
                ClimaUtilities.getCursorDouble(cursor, ClimaDB.SHIRTS_KEY_MIN_TEMP),
                ClimaUtilities.getCursorDouble(cursor, ClimaDB.SHIRTS_KEY_MAX_TEMP),
                ClimaUtilities.getCursorString(cursor, ClimaDB.SHIRTS_KEY_SLEEVE_TYPE),
                ClimaUtilities.getCursorLong(cursor, ClimaDB.SHIRTS_KEY_ID));
        assertEquals(testTop.getAvailability(), ClimaUtilities.NOT_AVAILABLE_TAG);
    }

    public void testAddBottom() throws Exception{
        boolean success;
        //create blank bitmap image
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
        success = ClimaDB.addBottom(new ClimaClosetBottom(bmp, ClimaUtilities.AVAILABLE_TAG, "Red", "Shorts", 33.00, 99.00));
        assertEquals(success, true);
    }

    public void testClimaQueryBottom() throws Exception{
        Cursor cursor;
        cursor = ClimaDB.ClimaQueryBottom(55, "All");
        //give the query some time to process
        Thread.sleep(200);
        assertNotNull(cursor);
    }

    public void testMarkBottomItemDirty() throws Exception{
        boolean success;
        Cursor cursor;
        ClimaClosetBottom testBottom;
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(10, 10, conf);
        success = ClimaDB.addBottom(new ClimaClosetBottom(bmp, ClimaUtilities.AVAILABLE_TAG, "Red", "Shirt", 33.00, 99.00));
        Thread.sleep(500);
        assertEquals(success, true);
        cursor = ClimaDB.ClimaQueryBottom(55, "All");
        Thread.sleep(800);
        assertNotNull(cursor);
        cursor.moveToFirst();
        testBottom = new ClimaClosetBottom(ClimaUtilities.getCursorImage(cursor, ClimaClosetDB.BOTTOMS_KEY_PICTURE),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_AVAILABILITY),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_COLOR),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_TYPE),
                ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP),
                ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP),
                ClimaUtilities.getCursorLong(cursor, ClimaClosetDB.SHIRTS_KEY_ID));
        assertNotNull(testBottom);
        assertEquals(ClimaUtilities.AVAILABLE_TAG, testBottom.getAvailability());
        success = ClimaDB.markBottomItemDirty(testBottom);
        Thread.sleep(800);
        assertEquals(success, true);
        cursor = ClimaDB.ClimaQueryBottom(55, "All");
        Thread.sleep(800);
        cursor.moveToFirst();
        testBottom = new ClimaClosetBottom(ClimaUtilities.getCursorImage(cursor, ClimaClosetDB.BOTTOMS_KEY_PICTURE),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_AVAILABILITY),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_COLOR),
                ClimaUtilities.getCursorString(cursor, ClimaClosetDB.BOTTOMS_KEY_TYPE),
                ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MIN_TEMP),
                ClimaUtilities.getCursorDouble(cursor, ClimaClosetDB.BOTTOMS_KEY_MAX_TEMP),
                ClimaUtilities.getCursorLong(cursor, ClimaClosetDB.SHIRTS_KEY_ID));
        assertEquals(testBottom.getAvailability(), ClimaUtilities.NOT_AVAILABLE_TAG);
    }
}
