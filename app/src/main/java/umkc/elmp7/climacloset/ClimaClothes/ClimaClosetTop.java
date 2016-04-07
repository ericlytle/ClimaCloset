package umkc.elmp7.climacloset.ClimaClothes;

import android.graphics.Bitmap;


/**
 * Created by ericl on 2/15/2016.
 */
public class ClimaClosetTop implements ClothingItem{
    //implement later
    private Bitmap topPicture;
    private String availability;
    private String color;
    private String topType;
    private double minTemp;
    private double maxTemp;
    private String sleeveType;
    private long ID;

    public ClimaClosetTop(Bitmap picture, String availability, String color, String topType, double minTemp, double maxTemp, String sleeveType){
        this.topPicture = picture;
        this.availability = availability;
        this.color = color;
        this.topType = topType;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.sleeveType = sleeveType;
    }
    public ClimaClosetTop(Bitmap picture, String availability, String color, String topType, double minTemp, double maxTemp, String sleeveType, long ID){
        this.ID = ID;
        this.topPicture = picture;
        this.availability = availability;
        this.color = color;
        this.topType = topType;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.sleeveType = sleeveType;
    }

    //get Methods
    public String getSleeveType(){
        return sleeveType;
    }

    @Override
    public Bitmap getPicture(){
        return topPicture;
    }

    @Override
    public String getAvailability(){
        return availability;
    }

    @Override
    public String getColor(){
        return color;
    }

    public String getTopType(){
        return topType;
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
