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
}
