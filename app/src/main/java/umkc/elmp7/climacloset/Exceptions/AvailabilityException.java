package umkc.elmp7.climacloset.Exceptions;

/**
 * Created by ericl on 4/6/2016.
 */
public class AvailabilityException extends Exception {
    private String availability;

    public AvailabilityException(String availability)
    {
        this.availability = availability;
    }

    @Override
    public String getMessage(){
        return "An invalid availability was received. \nAvailability: " + this.availability;
    }
}
