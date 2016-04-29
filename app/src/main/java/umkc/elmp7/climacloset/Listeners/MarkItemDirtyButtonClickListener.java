package umkc.elmp7.climacloset.Listeners;

import android.content.Context;
import java.util.Observable;

import android.util.Log;
import android.view.View;

import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetBottom;
import umkc.elmp7.climacloset.ClimaClothes.ClimaClosetTop;
import umkc.elmp7.climacloset.ClimaDB.ClimaClosetDB;
import umkc.elmp7.climacloset.ClimaUtil.ClimaUtilities;
import umkc.elmp7.climacloset.Exceptions.UpdateException;

/**
 * Created by Eric on 4/25/16.
 */
public class MarkItemDirtyButtonClickListener extends Observable implements View.OnClickListener {
        private ClimaClosetDB DB;
        private String itemType;
        public MarkItemDirtyButtonClickListener(Context context, String itemType) {
            this.DB = ClimaClosetDB.instance(context);
            this.itemType = itemType;
        }
        @Override
        public void onClick(View v) {

            try{
                if (itemType.equals(ClimaClosetDB.SHIRTS_TABLE)) {
                    ClimaClosetTop topTag = (ClimaClosetTop) v.getTag();
                    DB.markTopItemDirty(topTag);
                }
                else{
                    ClimaClosetBottom bottomTag = (ClimaClosetBottom) v.getTag();
                    DB.markBottomItemDirty(bottomTag);
                }
                setChanged();
                notifyObservers("Item updated!");
            }
            catch (UpdateException e)
            {
                Log.d("UpdateException", e.getMessage());
            }
        }
    }
