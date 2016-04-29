package umkc.elmp7.climacloset.ClimaClothes;

import android.graphics.Bitmap;

/**
 * Created by ericl on 2/15/2016.
 */
public interface ClothingItem {
    Bitmap getPicture();

    String getAvailability();

    String getColor();

    double getMinTemp();

    double getMaxTemp();

    long getID();
}
