package umkc.elmp7.climacloset.ClimaClothes;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetTop implements ClothingItem{
    //implement later
    private byte[] topPicture;
    public String getSleeveType(){
        return "";
    }

    @Override
    public byte[] getPicture(){
        return topPicture;
    }

    @Override
    public String getAvailability(){
        return "";
    }

    @Override
    public String getColor(){
        return "";
    }

    public String getTopType(){
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
