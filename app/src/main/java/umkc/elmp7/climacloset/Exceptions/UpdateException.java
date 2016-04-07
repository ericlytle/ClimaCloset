package umkc.elmp7.climacloset.Exceptions;

/**
 * Created by ericl on 4/6/2016.
 */
public class UpdateException extends Exception{
    private long itemID;
    private String table;

    public UpdateException(long ID, String Table){
        this.itemID = ID;
        this.table = Table;
    }

    @Override
    public String getMessage(){
        return "No items where updated from the " + this.table + " table with ID: "
                 + this.itemID;
    }
}
