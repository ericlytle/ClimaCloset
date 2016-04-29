package umkc.elmp7.climacloset.Listeners;

import android.content.Context;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Observable;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaClothes.ClothingItem;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.DeleteItemException;

/**
 * Created by Eric on 4/25/16.
 */
public class DeleteItemButtonClickListener extends Observable implements View.OnClickListener {
    private ClimaClosetDB climaClosetDB;
    private String itemTable;
    private View snackBarView;
    private Button markItemDirtyButton, deleteItemButton;

    public DeleteItemButtonClickListener(Context Context, String ItemType, View view, Button MarkItemDirtyButton, Button DeleteItemButton) {
        climaClosetDB = ClimaClosetDB.instance(Context);
        itemTable = ItemType;
        snackBarView = view;
        markItemDirtyButton = MarkItemDirtyButton;
        deleteItemButton = DeleteItemButton;
    }
    @Override
    public void onClick(View view) {
        ClothingItem itemTag = (ClothingItem) view.getTag();
        try {
            climaClosetDB.deleteItem(itemTag, itemTable);

            Snackbar.make(snackBarView, "Item deleted successfully", Snackbar.LENGTH_LONG)
                    .show();
            if (ClimaUtilities.ASSERTIONS_ENABLED) {
                assert markItemDirtyButton.getVisibility() == View.INVISIBLE : "DeleteTopButtonListener -- Mark item dirty button invisible";
                assert deleteItemButton.getVisibility() == View.INVISIBLE : "DeleteTopButtonListener -- delete item button invisible";
            }
            setChanged();
            notifyObservers("Item Deleted!");
        }
        catch (DeleteItemException e)
        {
            Log.d("DeleteItemException", e.getMessage());
        }
    }
}
