package umkc.elmp7.climacloset;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetBottom implements ClothingItem{
    private byte[] bottomPicture;

    @Override
    public byte[] getPicture(){
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
    public String getMinTemp(){
        return "";
    }

    @Override
    public String getMaxTemp(){
        return "";
    }
}
