package umkc.elmp7.climacloset.Listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Observable;

/**
 * Created by Eric on 4/25/16.
 */
public class FilterSpinnerItemSelectedListener extends Observable implements AdapterView.OnItemSelectedListener {
    private int itemSelected;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItemPosition() != itemSelected){
            setChanged();
            notifyObservers("Filter Applied");
            itemSelected = adapterView.getSelectedItemPosition();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
