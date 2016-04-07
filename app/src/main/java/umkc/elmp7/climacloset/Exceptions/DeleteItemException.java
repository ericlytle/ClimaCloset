package umkc.elmp7.climacloset.Exceptions;

import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;

/**
 * Created by ericl on 4/5/2016.
 */
public class DeleteItemException extends Exception {
    private ClothingItem clothingItem;
    private String table;
    public DeleteItemException(ClothingItem Item, String Table){
        this.clothingItem = Item;
        this.table = Table;
    }

    @Override
    public String getMessage(){
        return "Failed to delete clothing item from " + table + " with item ID: " + this.clothingItem.getID();
    }
}
