package umkc.elmp7.climacloset.ClimaClothes;

import android.graphics.Bitmap;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetBottom implements ClothingItem{
    private Bitmap bottomPicture;

    @Override
    public Bitmap getPicture(){
        return bottomPicture;
    }

    @Override
    public String getAvailability(){
        return "";
    }

    @Override
    public String getColor(){
        return "";
    }

    public String getBottomType(){
        return "";
    }

    @Override
    public double getMinTemp(){
        return 0.0;
    }

    @Override
    public double getMaxTemp(){
        return 0.0;
    }
}
