package umkc.elmp7.climacloset.ClimaClothes;

/**
 * Created by ericl on 2/15/2016.
 */
public interface ClothingItem {
    public byte[] getPicture();

    public String getAvailability();

    public String getColor();

    public String getMinTemp();

    public String getMaxTemp();
}
