package umkc.elmp7.climacloset.Listeners;

import android.content.Context;
import java.util.Observable;
import android.view.View;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.UpdateException;

/**
 * Created by Eric on 4/25/16.
 */
public class MarkItemDirtyButtonClickListener extends Observable implements View.OnClickListener {
        ClimaClosetDB DB;
        public MarkItemDirtyButtonClickListener(Context context) {
            DB = ClimaClosetDB.instance(context);
        }
        @Override
        public void onClick(View v) {
            ClimaClosetTop topTag = (ClimaClosetTop) v.getTag();
            try{
                if (topTag != null)
                {
//                    switch (topTag.getAvailability()){
//                        case (ClimaUtilities.AVAILABLE_TAG):
//                            topTag.updateAvailability(ClimaUtilities.NOT_AVAILABLE_TAG);
//                            break;
//                        case (ClimaUtilities.NOT_AVAILABLE_TAG):
//                            topTag.updateAvailability(ClimaUtilities.AVAILABLE_TAG);
//                            break;
//                    }
                }
                DB.markTopItemDirty(topTag);
//                loadPictures(filterSpinner.getSelectedItem().toString());
//                clearFields();
                //ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item updated!");
               // if (ClimaUtilities.ASSERTIONS_ENABLED) {
                    //assert markItemDirtyButton.getVisibility() == View.INVISIBLE : "MarkDirtyTopButtonListener -- Mark item dirty button invisible";
                    //assert deleteTopButton.getVisibility() == View.INVISIBLE : "MarkDirtyTopButtonListener -- delete item button invisible";
                //}
                setChanged();
                notifyObservers("Item updated!");
            }
            catch (UpdateException e)
            {
                //ClimaUtilities.SnackbarMessage(findViewById(android.R.id.content), "Item not updated!");
            }
        }
    }
