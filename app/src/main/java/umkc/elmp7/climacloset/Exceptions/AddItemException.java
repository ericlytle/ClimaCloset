package umkc.elmp7.climacloset.Exceptions;

import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;

/**
 * Created by ericl on 4/5/2016.
 */
public class AddItemException extends Exception{
    private ClothingItem clothingItem;
    private String table;

    public AddItemException(ClothingItem Item, String Table){
        this.clothingItem = Item;
        this.table = Table;
    }

    @Override
    public String getMessage(){
        return "Failed to add clothing item to " + table + " with item ID: " + this.clothingItem.getID();
    }
}
