package umkc.elmp7.climacloset.ClimaClothes;

import android.graphics.Bitmap;

/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetBottom implements ClothingItem{
    private Bitmap bottomPicture;
    private String availability;
    private String color;
    private String bottomType;
    private double minTemp;
    private double maxTemp;
    private long ID;

    public ClimaClosetBottom(Bitmap picture, String availability, String color, String bottomType, double minTemp, double maxTemp){
        this.bottomPicture = picture;
        this.availability = availability;
        this.color = color;
        this.bottomType = bottomType;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public ClimaClosetBottom(Bitmap picture, String availability, String color, String bottomType, double minTemp, double maxTemp, long ID){
        this.bottomPicture = picture;
        this.availability = availability;
        this.color = color;
        this.bottomType = bottomType;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.ID = ID;
    }

    @Override
    public Bitmap getPicture(){
        return bottomPicture;
    }

    @Override
    public String getAvailability(){
        return availability;
    }

    @Override
    public String getColor(){
        return color;
    }

    public String getBottomType(){
        return bottomType;
    }

    @Override
    public double getMinTemp(){
        return minTemp;
    }

    @Override
    public double getMaxTemp(){
        return maxTemp;
    }

    @Override
    public long getID(){return ID;}

    public void updateAvailability(String Availability){
        this.availability = Availability;
    }
}
