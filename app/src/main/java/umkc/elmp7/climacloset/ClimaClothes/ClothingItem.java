package umkc.elmp7.climacloset.ClimaClothes;

import android.graphics.Bitmap;

/**
 * Created by ericl on 2/15/2016.
 */
public interface ClothingItem {
    public Bitmap getPicture();

    public String getAvailability();

    public String getColor();

    public double getMinTemp();

    public double getMaxTemp();

    public long getID();
}
